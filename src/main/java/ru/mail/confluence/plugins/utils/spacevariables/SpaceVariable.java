package ru.mail.confluence.plugins.utils.spacevariables;

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

    long getSpaceId();
    void setSpaceId(long spaceId);

    boolean getDeleted();
    void setDeleted(boolean deleted);
}
