package com.flowdock.jenkins;

import hudson.model.AbstractBuild;
import hudson.model.Result;

public enum BuildResult {
    SUCCESS("was successful"),
    FAILURE("failed"),
    UNSTABLE("was unstable"),
    ABORTED("was aborted"),
    NOT_BUILT("was not built"),
    FIXED("was fixed"),
    BROKEN("was broken");

    private String humanResult;

    private BuildResult(String displayName) {
        this.humanResult = displayName;
    }

    public static BuildResult fromBuild(AbstractBuild build) {
        Result prevResult = build.getPreviousBuild() != null ? build.getPreviousBuild().getResult() : null;

        if (build.getResult().equals(Result.SUCCESS)) {
            if (Result.FAILURE.equals(prevResult) || Result.UNSTABLE.equals(prevResult)) {
                return FIXED;
            }
            return SUCCESS;
        } else if (build.getResult().equals(Result.UNSTABLE)) {
            return UNSTABLE;
        } else if (build.getResult().equals(Result.ABORTED)) {
            return ABORTED;
        } else if (build.getResult().equals(Result.NOT_BUILT)) {
            return NOT_BUILT;
        } else {
            if (!(Result.FAILURE.equals(prevResult) || Result.UNSTABLE.equals(prevResult))) {
                return BROKEN;
            }

            return FAILURE;
        }
    }

    public String getHumanResult() {
        return humanResult;
    }
}
