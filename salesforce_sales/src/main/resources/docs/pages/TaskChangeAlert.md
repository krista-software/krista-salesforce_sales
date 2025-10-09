# Task Change Alert

## Overview

The Task Change Alert catalog request monitors changes to task records in Salesforce Sales and sends notifications when specified task fields are modified. This enables real-time tracking of task progress and completion.

## Request Details

- **Catalog Request Name**: Task Change Alert
- **Description**: Monitor and receive alerts for task record changes
- **Type**: Alert/Notification
- **Trigger**: Task field modifications

## Alert Configuration

### Monitored Fields

| Field Name | Description | Alert Priority |
|------------|-------------|----------------|
| Status | Task status changes | High |
| Priority | Task priority changes | Medium |
| Due Date | Due date modifications | High |
| Assigned To Id | Task assignment changes | High |
| Title | Task title/subject changes | Low |

### Alert Triggers

| Trigger Type | Description |
|--------------|-------------|
| Status Change | Task status updates (Not Started → In Progress → Completed) |
| Assignment Change | Task reassigned to different user |
| Due Date Change | Due date modified or approaching |
| Priority Change | Task priority updated |
| Completion | Task marked as completed |
| Overdue | Task becomes overdue |

## Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| taskIds | Text Array | No | Specific task IDs to monitor |
| assignedToIds | Text Array | No | Monitor tasks assigned to specific users |
| priorities | PickOne Array | No | Monitor tasks with specific priorities |
| statuses | PickOne Array | No | Monitor tasks with specific statuses |
| fieldsToMonitor | Text Array | No | Specific fields to monitor for changes |
| notificationEmail | Email | No | Email address for alert notifications |
| webhookUrl | Text | No | Webhook URL for real-time notifications |
| alertFrequency | PickOne | No | Frequency (Immediate, Hourly, Daily) |

## Alert Payload

```json
{
  "alertType": "TaskChange",
  "timestamp": 1640995200000,
  "taskId": "00T5w00000ABC123",
  "taskTitle": "Follow up with Enterprise Customer",
  "changeType": "StatusChange",
  "changedFields": [
    {
      "fieldName": "Status",
      "oldValue": "In Progress",
      "newValue": "Completed",
      "changeTimestamp": 1640995200000
    }
  ],
  "taskDetails": {
    "priority": "High",
    "dueDate": 1640995200000,
    "assignedToId": "0055w00000ABC123",
    "assignedToName": "John Smith"
  }
}
```

## Common Use Cases

1. **Task Completion Tracking**: Monitor when important tasks are completed
2. **Overdue Management**: Get alerts for overdue tasks
3. **Assignment Monitoring**: Track task reassignments
4. **Priority Management**: Monitor changes to task priorities
5. **Project Tracking**: Track tasks related to specific records

## See Also

- [Task Entity](pages/entities/Task.md)
- [Get Task Statuses](pages/GetTaskStatuses.md)
- [User Entity](pages/entities/User.md)
