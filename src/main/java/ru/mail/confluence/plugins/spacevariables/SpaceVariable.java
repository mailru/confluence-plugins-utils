package ru.mail.confluence.plugins.spacevariables;

import net.java.ao.Entity;
import net.java.ao.schema.StringLength;

public interface SpaceVariable extends Entity {
    String getName();
    void setName(String name);

    long getPageId();
    void setPageId(long pageId);

    @StringLength(StringLength.UNLIMITED)
    String getDescription();
    void setDescription(String description);

    String getSpaceKey();
    void setSpaceKey(String spaceKey);
}
