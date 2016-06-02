package ru.mail.confluence.plugins.utils.spacevariables;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration"})
@XmlRootElement
public class SpaceVariableDto {
    @XmlElement
    private int id;
    @XmlElement
    private String name;
    @XmlElement
    private ConfluencePageDto page;
    @XmlElement
    private String description;
    @XmlElement
    private long spaceId;
    @XmlElement
    private String spaceKey;
    @XmlElement
    private boolean deleted;

    public SpaceVariableDto() {
    }

    public SpaceVariableDto(SpaceVariable spaceVariable) {
        this.id = spaceVariable.getID();
        this.name = spaceVariable.getName();
        this.description = spaceVariable.getDescription();
        this.spaceId = spaceVariable.getSpaceId();
        this.deleted = spaceVariable.getDeleted();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfluencePageDto getPage() {
        return this.page;
    }

    public void setPage(ConfluencePageDto page) {
        this.page = page;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSpaceId() {
        return this.spaceId;
    }

    public void setSpaceId(long spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceKey() {
        return this.spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
