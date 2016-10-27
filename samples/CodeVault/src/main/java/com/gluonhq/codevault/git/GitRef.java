package com.gluonhq.codevault.git;

import org.eclipse.jgit.lib.Ref;

public class GitRef {

    protected Ref ref;

    public GitRef(Ref ref) {
        this.ref = ref;
    }

    public String getId() {
        return ref.getObjectId().getName();
    }

    public String getFullName() {
        return ref.getName();
    }

    public String getShortName() {
        String[] path = getFullName().split("/");
        return path.length == 0? getFullName(): path[path.length-1];
    }

    @Override
    public String toString() {
        return getShortName();
    }
}
