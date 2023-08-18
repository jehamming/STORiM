package com.hamming.storim.server;

import com.hamming.storim.server.common.model.BasicObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuthorisationController {

    private HashMap<Class, List<AuthorisationListener>> authorisationListeners;

    public AuthorisationController() {
        authorisationListeners = new HashMap<>();
    }

    public void addAuthorisationListener(Class clazz, AuthorisationListener listener) {
        List<AuthorisationListener> classListeners = authorisationListeners.get(clazz);
        if ( classListeners == null ) {
            classListeners = new ArrayList<>();
            authorisationListeners.put(clazz, classListeners);
        }
        classListeners.add(listener);
    }

    public void removeAuthorisationListener(Class clazz, AuthorisationListener listener) {
        List<AuthorisationListener> classListeners = authorisationListeners.get(clazz);
        if ( classListeners != null ) {
            classListeners.remove(listener);
        }
    }
    public void fireAuthorisationChanged(BasicObject o, List<Long> oldEditors) {
        List<AuthorisationListener> classListeners = authorisationListeners.get(o.getClass());
        if ( classListeners != null ) {
            for (AuthorisationListener l : classListeners) {
                l.authorisationChanged(o,oldEditors);
            }
        }
    }
    public AuthorisationDelta getAuthorisationDelta(List<Long> oldEditors, List<Long> newEditors){
        List<Long> added = new ArrayList<>();
        List<Long> removed = new ArrayList<>();
        for (Long id : oldEditors ) {
            if ( !newEditors.contains(id)) {
                removed.add(id);
            }
        }
        for (Long id : newEditors ) {
            if ( !oldEditors.contains(id)) {
                added.add(id);
            }
        }
        AuthorisationDelta delta = new AuthorisationDelta(added, removed);
        return delta;
    }
}
