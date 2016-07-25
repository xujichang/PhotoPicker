package me.iwf.photopicker.entity;

/**
 * Created by xujichang on 2016/7/25.
 */
public abstract class BaseDirectory {
    public static final int DIRESTORY_PHOTO = 1;
    public static final int DIRESTORY_VIDEO = 2;
    protected int directoryType = 1;
    protected String id;
    protected String coverPath;
    protected String name;
    protected long dateAdded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDirectory that = (BaseDirectory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    abstract void setDirectoryType();

    public int getDirectoryType() {
        return directoryType;
    }
}
