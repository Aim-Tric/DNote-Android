package top.devonte.note.bean;

import java.util.Date;

public class FileBean {

    private Integer id;
    private Integer folderId;
    private String title;
    private int type;
    private String body;
    private boolean isTopping;
    private boolean isFolder;
    private Date created;
    private Date updated;
    private Date topped;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isTopping() {
        return isTopping;
    }

    public void setTopping(boolean topping) {
        isTopping = topping;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getTopped() {
        return topped;
    }

    public void setTopped(Date topped) {
        this.topped = topped;
    }
}
