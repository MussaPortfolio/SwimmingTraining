package com.example.swimmingtraining;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/** Tracks Firebase listeners so an Activity can detach them all in onDestroy(). */
class FirebaseListeners {
    private final List<DatabaseReference> refs = new ArrayList<>();
    private final List<ValueEventListener> listeners = new ArrayList<>();

    void add(DatabaseReference ref, ValueEventListener listener) {
        ref.addValueEventListener(listener);
        refs.add(ref);
        listeners.add(listener);
    }

    void removeAll() {
        for (int i = 0; i < refs.size(); i++) {
            refs.get(i).removeEventListener(listeners.get(i));
        }
        refs.clear();
        listeners.clear();
    }
}
