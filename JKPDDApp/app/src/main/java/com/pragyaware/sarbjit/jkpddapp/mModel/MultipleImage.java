package com.pragyaware.sarbjit.jkpddapp.mModel;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/10/2017.
 */
public class MultipleImage extends RealmObject {

    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
