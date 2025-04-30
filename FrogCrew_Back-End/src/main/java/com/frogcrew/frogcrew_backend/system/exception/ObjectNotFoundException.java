package com.frogcrew.frogcrew_backend.system.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with Id " + id + " :(");
    }

    public ObjectNotFoundException(String objectName, String item_type, String item_val ) {

        super("Could not find " + objectName + " with " + item_type + " " + item_val + " :(");

    }

    public ObjectNotFoundException(String objectName, String item_type, Integer item_val ) {
        super("Could not find " + objectName + " with " + item_type + " " + item_val + " :(");
    }

}