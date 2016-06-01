package ru.mail.confluence.plugins.utils.spacevariables;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration"})
@XmlRootElement
public class ConfluencePageDto {
    @XmlElement
    private long id;
    @XmlElement
    private String title;
    @XmlElement
    private String url;
    @XmlElement
    private String spaceKey;
    @XmlElement
    private String spaceName;

    public ConfluencePageDto() {
    }

    public ConfluencePageDto(long id, String title, String url, String spaceKey, String spaceName) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.spaceKey = spaceKey;
        this.spaceName = spaceName;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.title = url;
    }

    public String getSpaceKey() {
        return this.spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getSpaceName() {
        return this.title;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }
}
