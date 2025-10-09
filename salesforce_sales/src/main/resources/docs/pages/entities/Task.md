# Task Entity

## Overview

The Task entity represents activities and to-do items in Salesforce Sales. Tasks help sales teams track follow-up activities, manage their daily work, and ensure important actions are completed. They can be associated with various records like Accounts, Opportunities, Leads, and Contacts.

## Entity Details

- **Entity Name**: Task
- **Primary Key**: Id
- **Searchable Fields**: Title, Priority, Due Date, Status, Assigned To Name
- **Display Fields**: Title, Priority, Due Date, Status
- **Store Support**: No

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Task record |
| Title | Text | Yes | Subject or title of the task (searchable, used for display) |
| Description | Paragraph | No | Detailed description of the task (used for display) |
| Status | Text | Yes | Current status of the task (searchable, used for display) |
| Task Type | Text | No | Type or category of the task |

### Priority and Scheduling

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Priority | PickOne | Yes | Priority level (searchable, used for display) |
| Due Date | Date | No | When the task is due (searchable, used for display, includes time) |

### Priority Values

The Priority field accepts the following predefined values:
- **Low**: Non-urgent tasks that can be completed when time permits
- **Normal**: Standard priority tasks for regular workflow
- **High**: Urgent tasks requiring immediate attention

### Assignment

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Assigned To Id | Text | Yes | ID of the user assigned to complete the task |
| Assigned To Name | Text | No | Name of the assigned user (searchable, used for display) |

### Associated Records

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Associated Entity Id | Text | No | ID of the related record (Account, Opportunity, etc.) |
| Associated Entity Type | Text | No | Type of the associated record |
| Associated Entity Name | Text | No | Name of the associated record |

## Task Status Values (Common)

- **Not Started**: Task has been created but work hasn't begun
- **In Progress**: Task is currently being worked on
- **Completed**: Task has been finished successfully
- **Waiting on someone else**: Task is blocked waiting for others
- **Deferred**: Task has been postponed to a later date

## Associated Entity Types

Tasks can be associated with various Salesforce objects:

- **Account**: Tasks related to account management
- **Opportunity**: Tasks for advancing sales opportunities
- **Lead**: Tasks for lead qualification and follow-up
- **Contact**: Tasks for contact relationship management
- **Case**: Tasks for customer support activities

## Usage in Catalog Requests

The Task entity is used in the following catalog requests:

- **Get Task Statuses**: Retrieve available task status values
- **Task Change Alert**: Monitor changes to task records

## Task Management Workflow

### Task Creation
1. **Identify Need**: Determine what action needs to be taken
2. **Create Task**: Set title, description, priority, and due date
3. **Assign Owner**: Assign to appropriate team member
4. **Link Records**: Associate with relevant accounts, opportunities, etc.

### Task Execution
1. **Review Tasks**: Check assigned tasks and priorities
2. **Update Status**: Mark as "In Progress" when starting work
3. **Complete Work**: Perform the required actions
4. **Mark Complete**: Update status to "Completed"

### Task Monitoring
1. **Due Date Tracking**: Monitor upcoming and overdue tasks
2. **Priority Management**: Focus on high-priority items first
3. **Status Updates**: Keep task status current
4. **Performance Review**: Analyze task completion rates

## Validation Rules

- **Title**: Required field, cannot be empty
- **Priority**: Must be one of the predefined values (Low, Normal, High)
- **Status**: Required field, cannot be empty
- **Assigned To Id**: Required field, must reference a valid user
- **Due Date**: Must be a valid date/time if provided

## Best Practices

1. **Clear Titles**: Use descriptive titles that explain the action needed
2. **Realistic Due Dates**: Set achievable deadlines
3. **Priority Assignment**: Use priorities consistently across the team
4. **Regular Updates**: Keep task status current
5. **Record Association**: Link tasks to relevant records for context
6. **Follow-up Tasks**: Create follow-up tasks for ongoing activities

## Common Use Cases

1. **Sales Follow-up**: Schedule calls and meetings with prospects
2. **Opportunity Management**: Track next steps for active deals
3. **Lead Nurturing**: Plan touchpoints with potential customers
4. **Account Management**: Schedule regular check-ins with customers
5. **Administrative Tasks**: Track internal activities and deadlines
6. **Team Coordination**: Assign and track team member activities

## Task Categories (Examples)

### Sales Activities
- Call prospect
- Send proposal
- Schedule demo
- Follow up on quote
- Prepare presentation

### Administrative Tasks
- Update CRM data
- Prepare reports
- Review contracts
- Process paperwork
- Schedule meetings

### Customer Service
- Follow up on support case
- Check customer satisfaction
- Schedule training session
- Review account status
- Plan renewal discussion

## Example Usage

```json
{
  "id": "00T5w00000ABC123",
  "title": "Follow up on Enterprise Software Proposal",
  "description": "Call John Smith at Acme Corp to discuss the enterprise software proposal sent last week. Address any questions about pricing and implementation timeline.",
  "priority": "High",
  "status": "Not Started",
  "dueDate": 1640995200000,
  "assignedToId": "0055w00000ABC123",
  "assignedToName": "Sarah Johnson",
  "associatedEntityId": "0065w00000ABC123",
  "associatedEntityType": "Opportunity",
  "associatedEntityName": "Acme Corp - Enterprise Software",
  "taskType": "Call"
}
```

## Task Alert Example

When using Task Change Alert, you might receive notifications like:

```json
{
  "taskId": "00T5w00000ABC123",
  "taskTitle": "Follow up on Enterprise Software Proposal",
  "taskPriority": "High",
  "taskDueDate": 1640995200000,
  "taskDescription": "Call John Smith at Acme Corp...",
  "taskStatus": "Completed",
  "taskAssignedToId": "0055w00000ABC123",
  "taskAssociatedEntityId": "0065w00000ABC123"
}
```

## See Also

- [User Entity](pages/entities/User.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Account Entity](pages/entities/Account.md)
- [Lead Entity](pages/entities/Lead.md)
- [Get Task Statuses](pages/GetTaskStatuses.md)
- [Task Change Alert](pages/TaskChangeAlert.md)
