package com.emc.xsdviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.emc.xsdviewer.parser.Settings;
import com.emc.xsdviewer.parser.XsdTreeObject;


@RestController
public class Controller {

    private final MongoDB db = new MongoDB();
    private XsdTreeObject tree;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Set<String> getAll() {
        return db.getAllNames();
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> get(final @PathVariable String name,
                                 final @RequestParam(value = "attributes", required = false)
                                         Set<String> attributes) {

        Settings settings = new Settings();
        InputStream inputStream = db.getInputStream(name);
        if (attributes != null) {
            settings.deleteNodeName(attributes);
        }

        if (inputStream != null) {
            tree = new XsdTreeObject(db.getInputStream(name));
            return new ResponseEntity<>(tree.getTree(settings), HttpStatus.OK);
        }
        return new ResponseEntity<>("XSD with name: " + name + " not found\n", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/allAttributes", method = RequestMethod.GET)
    public Set<String> get() {
        return db.getAllAttributes();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(final @RequestParam("xsdScheme") MultipartFile uploadFile,
                                 final @RequestParam("name") String name) {

        String content;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);

        if (db.checkName(name)) {
            content = "This name (" + name + ") is already taken";
            return new ResponseEntity<>(content, responseHeaders, HttpStatus.BAD_REQUEST);
        }
        try {
            db.addFile(uploadFile, name);
        } catch (final IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        content = "XSD-file added\n";
        return new ResponseEntity<>(content, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(final @PathVariable String id,
                                    final @RequestBody XsdViewComposition xsd) {
        db.updateNameByID(id, xsd.getName());
        return new ResponseEntity<>(db.getByID(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(final @PathVariable String id) {
        db.removeByID(id);
        //TODO: create notification, if xsd is not exist
        return new ResponseEntity<>("XSD with id: " + id + " removed\n", HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete() {
        db.clear();
        return new ResponseEntity<>("BD is empty\n", HttpStatus.OK);
    }
}