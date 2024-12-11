package com.surge.loanManagement.DTO;

import java.sql.Timestamp;

public class TaskDTO {
    private String taskId;
    private String taskName;
    private String assignee;
    private String processInstanceId;
    private Timestamp creationTimestamp;

    public TaskDTO(String taskId, String taskName, String assignee, String processInstanceId,Timestamp creationTimestamp) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.assignee = assignee;
        this.processInstanceId = processInstanceId;
        this.creationTimestamp = creationTimestamp;
    }

	public String getTaskId() {
		return taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getAssignee() {
		return assignee;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

}