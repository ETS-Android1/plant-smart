package com.example.plantsmart;

import android.net.Uri;

public class plant {
    String name;
    Uri pic;

    public String getName() {
        return name;
    }

    public Uri getPic() {
        return pic;
    }

    public plant(String name, Uri pic) {
        this.name = name;
        this.pic = pic;
    }
}
