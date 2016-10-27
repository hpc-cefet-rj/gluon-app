package com.gluonhq.codevault.git;

import org.eclipse.jgit.lib.Ref;

public class GitBranch extends GitRef {

    public GitBranch(Ref ref) {
        super(ref);
    }
}
