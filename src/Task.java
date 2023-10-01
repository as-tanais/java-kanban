public class Task {

    protected String title;
    protected String description;
    protected int id;
    protected String  status;
    protected String  type;



    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "NEW";
        this.type = "TASK";
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "id='" + id +
                "', title='" + title +
                "', description='" + description +
                "', status='" + status + "'}";
        return result;
    }


}
