package no.fjordkraft.im.jobs.domain;

public class JobFetchRequest {

    private boolean fetchJobResults;

    public JobFetchRequest() {
    }

    public JobFetchRequest(boolean fetchJobResults) {
        this.fetchJobResults = fetchJobResults;
    }

    public boolean isFetchJobResults() {
        return fetchJobResults;
    }

    public void setFetchJobResults(boolean fetchJobResults) {
        this.fetchJobResults = fetchJobResults;
    }
}
