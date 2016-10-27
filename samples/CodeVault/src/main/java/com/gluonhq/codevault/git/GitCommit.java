package com.gluonhq.codevault.git;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;
import java.util.List;

public class GitCommit {

    private RevCommit commit;
    private List<GitRef> refs;
    public GitCommit(RevCommit c, List<GitRef> refs ) {
        this.commit = c;
        this.refs = refs;
    }

    public List<GitRef> getRefs() {
        return refs;
    }

    public String getShortMessage() {
        return commit.getShortMessage();
    }

    public String getFullMessage() {
        return commit.getFullMessage();
    }

    public String getId() {
        return commit.getId().getName();
    }

    public String getHash() {
        return getId().substring(0,8);
    }

    public Date getTime() {
        return commit.getAuthorIdent().getWhen();
    }

    public String getAuthor() {
        PersonIdent personId = commit.getAuthorIdent();
        return String.format( "%s<%s>",  personId.getName(), personId.getEmailAddress());
    }
}
