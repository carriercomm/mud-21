package mud;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Embedded
public class MudExit {
    @Reference
    private MudRoom destination;
    private String description;
    private boolean isLockable;
    private boolean isLocked;
    private boolean isVisible;          // if this is false, then only players in the visibleTo array can see this item
    @Reference
    private List<MudPlayer> visibleTo;  // if not visible, list of who can see it
    private List<String> tags;          // function tags
    private Date lastUsed;

    public MudExit() {
        description = "You can't see to well that way.";
        isLockable = false;
        isLocked = false;
        isVisible = true;
        visibleTo = new ArrayList<MudPlayer>();
        tags = new ArrayList<String>();
    }

    public MudRoom getDestination() {
        return destination;
    }

    public void setDestination(MudRoom room) {
        destination = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsLockable() {
        return isLockable;
    }

    public void setIsLockable(boolean isLockable) {
        this.isLockable = isLockable;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean getIsVisibleTo(MudPlayer player) {
        if (isVisible || visibleTo.contains(player))
            return true;
        return false;
    }

    public void setIsVisibleTo(MudPlayer player) {
        if (!getIsVisibleTo(player))
            visibleTo.add(player);
    }

    public boolean hasTag(String tag) {
        if (tags.contains(tag))
            return true;
        return false;
    }

    public void addTagIfNotExists(String tag) {
        if (!tags.contains(tag))
            tags.add(tag);
    }

    public void removeTagIfExists(String tag) {
        if (hasTag(tag))
            tags.remove(tag);
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void updateLastUsed() {
        lastUsed = new Date();
    }
}
