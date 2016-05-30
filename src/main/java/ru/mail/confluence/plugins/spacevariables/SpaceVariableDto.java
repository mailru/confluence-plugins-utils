package ru.mail.confluence.plugins.spacevariables;

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
    private String spaceKey;

    public SpaceVariableDto() {
    }

    public SpaceVariableDto(SpaceVariable spaceVariable) {
        this.id = spaceVariable.getID();
        this.name = spaceVariable.getName();
        this.description = spaceVariable.getDescription();
        this.spaceKey = spaceVariable.getSpaceKey();
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

    public String getSpaceKey() {
        return this.spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }
}
