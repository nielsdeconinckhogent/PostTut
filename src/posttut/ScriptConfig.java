package posttut;

class ScriptConfig {

    private final Object synchronizer;
    private String status = "IDLE";
    private String step = "0";

    public ScriptConfig() {
        this.synchronizer = new Object();
    }

    public void setStatus(String status) {
        synchronized (synchronizer) {
            this.status = status;
        }
    }

    public String getStatus() {
        synchronized (synchronizer) {
            return this.status;
        }
    }
}