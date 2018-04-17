package com.emc.xsdviewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Created by nika- on 18.07.2017.
 */

public class MongoDB {

    private DBCollection xsdCollection;
    private final GridFS gridfs;
    private static final String COLLECTION_NAME = "xsd_files";
    private static final String ID_KEY = "_id";

    public MongoDB() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB db = mongoClient.getDB("xsdDB");
        xsdCollection = db.getCollection(COLLECTION_NAME);
        if (xsdCollection == null) {
            xsdCollection = db.createCollection(COLLECTION_NAME, null);
        }
        gridfs = new GridFS(db, COLLECTION_NAME);
    }

    public void add(final XsdViewComposition xsd) {
        XsdViewComposition newXsd = new XsdViewComposition(xsd.getName(), xsd.getXsdSchema());
        BasicDBObject doc = newXsd.toDBObject();
        xsdCollection.insert(doc);
    }

/*    public List<XsdViewComposition> getAll() {
        List<XsdViewComposition> xsdFiles = new ArrayList<XsdViewComposition>();
        DBCursor cursor = xsdCollection.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            xsdFiles.add(XsdViewComposition.fromDBObject(dbo));
        }
        return xsdFiles;
    }*/

    public Set<String> getAllNames() {
        Set<String> xsdFiles = new HashSet<>();
        DBCursor cursor = gridfs.getFileList();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            xsdFiles.add((String)object.get("filename"));
        }
        return xsdFiles;
    }

    public XsdViewComposition getByID(final String id) {
        DBObject result = findRecordByID(id);
        if (result != null) {
            return XsdViewComposition.fromDBObject(result);
        }
        return null;
    }

    public void updateNameByID(final String id, final String newName) {
        BasicDBObject newData = new BasicDBObject();
        newData.put("_name", newName);
        newData.put(ID_KEY, id);
        BasicDBObject searchQuery = new BasicDBObject().append(ID_KEY, id);
        xsdCollection.update(searchQuery, newData);
    }

    public void removeByID(final String id) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID_KEY, id);
        xsdCollection.remove(query);
    }

    public void clear() {
        xsdCollection.remove(new BasicDBObject());
        gridfs.remove(new BasicDBObject());
    }

    private DBObject findRecordByID(final String id) {
        BasicDBObject query = new BasicDBObject();
        query.put(ID_KEY, id);
        return xsdCollection.findOne(query);
    }

    public void addFile(final MultipartFile multipartFile, final String name) throws IOException {
        InputStream inputStream = getInputStreamFromMultipartFile(multipartFile);
        GridFSInputFile inputFile = gridfs.createFile(inputStream, name);
        inputFile.save();
    }

/*    public String getFile(final String name) {
        StringWriter sw = new StringWriter();
        InputStream inputStream = getInputStreamFromGridFSD(name);
        String readLine;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (((readLine = bufferedReader.readLine()) != null)) {
                System.out.println(readLine);
                sw.append(readLine);
            }
        } catch (Exception e) {
            //LOG????
        }
        return sw.toString();
    }*/

/*    public String getByName(final String name) {
        return getFile(name);
    }*/

    public InputStream getInputStream(final String name) {
        return getInputStreamFromGridFSD(name);
    }

    public Set<String> getAllAttributes() {
        //TODO: To register a file in the database, with a list of attributes; In this method, get the file from the database
        return new HashSet<String>(Arrays.asList("a attribute", "b attribute", "c attribute"));
    }

    private InputStream getInputStreamFromMultipartFile(final MultipartFile multipartFile) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
        byte[] file = new byte[inputStream.available()];
        inputStream.read(file);
        return new ByteArrayInputStream(file);
    }

    private InputStream getInputStreamFromGridFSD(final String name) {
        GridFSDBFile gfsFileOut = (GridFSDBFile)gridfs.findOne(name);
        if (gfsFileOut == null) {
            return null;
        }
        return gfsFileOut.getInputStream();
    }

    public boolean checkName(final String name) {
        return getAllNames().contains(name);
    }
}