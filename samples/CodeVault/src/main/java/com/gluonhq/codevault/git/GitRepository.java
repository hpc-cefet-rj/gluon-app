package com.gluonhq.codevault.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GitRepository {

    private static final String GIT_FOLDER_NAME = ".git";

    private Repository repo;
    private Git git;
    private final File location;

    public GitRepository(File location) throws GitRepoException {

        this.location = Objects.requireNonNull(location);

        if ( !isGitRepo(location)) {
            throw new GitRepoException("Git repository not found at " + location);
        }


        File gitDir =
                GIT_FOLDER_NAME.equals(location.getName()) ? location : new File(location, GIT_FOLDER_NAME);

        try {
            repo = new FileRepositoryBuilder()
                    .setGitDir(gitDir)
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir()      // scan up the file system tree
                    .build();

            git = new Git(repo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitRepository that = (GitRepository) o;

        return location.equals(that.location);

    }

    public void close() {
        repo.close();
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    private boolean isGitRepo( File location ) {
        return (location.exists() && location.getName().endsWith(GIT_FOLDER_NAME)) ||
                (new File( location, GIT_FOLDER_NAME).exists());
    }

    public File getLocation() {
        return location;
    }

    public String getName() {
        return location.getName();
    }

    public Collection<GitCommit> getLog() {
        try {
            Collection<GitRef> refs = new HashSet<>(getBranches());
            refs.addAll(getTags());

            Map<String, List<GitRef>> refMap = refs
                    .stream()
                    .collect(Collectors.groupingBy(GitRef::getId));

            return StreamSupport
                    .stream(git.log().all().call().spliterator(), false)
                    .map( c -> new GitCommit(c, refMap.getOrDefault(c.getId().getName(), Collections.emptyList())))
                    .collect(Collectors.toList());
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Collection<GitBranch> getBranches() {
        try {
            return StreamSupport
                    .stream(git.branchList().call().spliterator(), false)
                    .map(ref -> new GitBranch(ref))
                    .collect(Collectors.toSet());
        } catch (GitAPIException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public Collection<GitTag> getTags() {
        try {
            return StreamSupport
                    .stream(git.tagList().call().spliterator(), false)
                    .map(ref -> new GitTag(!ref.isPeeled() ? ref : repo.peel(ref)))
                    .collect(Collectors.toSet());
        } catch (GitAPIException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
