package com.gluonhq.codevault.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;

public class GitTag extends GitRef {

    public GitTag(Ref ref) {
        super(ref);
    }

    @Override
    public String getId() {
        ObjectId peeledId = ref.getPeeledObjectId();
        return peeledId == null? super.getId(): peeledId.getName();
    }
}

