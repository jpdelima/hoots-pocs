package io.hoots.domain;

import java.util.UUID;

/**
 * Created by rwadowski on 13.04.17.
 */
public class Item {

    private final String name;
    private final UUID id;

    public Item(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public String name() {
        return this.name;
    }

    public UUID id() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if(null == object) {
            return false;
        }

        if(this == object) {
            return true;
        }

        if(!(object instanceof Item)) {
            return false;
        }

        final Item that = (Item) object;

        return this.name.equals(that.name)
            && this.id.equals(that.id);
    }

    @Override
    public String toString() {
        return "Item(" + this.name + ")";
    }
}
