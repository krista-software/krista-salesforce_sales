# Get Task Statuses

## Overview

The Get Task Statuses catalog request retrieves all available task status values from Salesforce Sales. This request provides the complete list of status options that can be used when creating or updating tasks.

## Request Details

- **Catalog Request Name**: Get Task Statuses
- **Description**: Retrieve all available task status values
- **Returns**: List of task status options
- **Type**: Reference Data Request

## Parameters

This request typically requires no parameters as it returns all available task statuses in the system.

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| includeInactive | Switch | No | Include inactive/deprecated status values (default: false) |
| language | Text | No | Language code for localized status labels |

## Response Format

Returns an array of task status objects with the following structure:

```json
{
  "taskStatuses": [
    {
      "value": "Not Started",
      "label": "Not Started",
      "isDefault": true,
      "isActive": true,
      "sortOrder": 1,
      "description": "Task has been created but work hasn't begun"
    },
    {
      "value": "In Progress", 
      "label": "In Progress",
      "isDefault": false,
      "isActive": true,
      "sortOrder": 2,
      "description": "Task is currently being worked on"
    }
  ]
}
```

## Standard Task Statuses

### Common Status Values

| Status Value | Description | Usage |
|--------------|-------------|-------|
| Not Started | Task created but not yet begun | Default status for new tasks |
| In Progress | Task is actively being worked on | When work has commenced |
| Completed | Task has been finished | When task is successfully completed |
| Waiting on someone else | Task blocked by external dependency | When waiting for others |
| Deferred | Task postponed to later date | When task is delayed |
| Cancelled | Task no longer needed | When task becomes irrelevant |

### Status Categories

**Active Statuses** (work in progress):
- Not Started
- In Progress  
- Waiting on someone else

**Completed Statuses** (work finished):
- Completed
- Cancelled

**Deferred Statuses** (work postponed):
- Deferred

## Example Usage

### Basic Request
```json
{
  // No parameters needed for basic request
}
```

### Include Inactive Statuses
```json
{
  "includeInactive": true
}
```

### Localized Labels
```json
{
  "language": "es"
}
```

## Response Examples

### Standard Response
```json
{
  "taskStatuses": [
    {
      "value": "Not Started",
      "label": "Not Started", 
      "isDefault": true,
      "isActive": true,
      "sortOrder": 1,
      "description": "Task has been created but work hasn't begun"
    },
    {
      "value": "In Progress",
      "label": "In Progress",
      "isDefault": false,
      "isActive": true,
      "sortOrder": 2,
      "description": "Task is currently being worked on"
    },
    {
      "value": "Completed",
      "label": "Completed",
      "isDefault": false,
      "isActive": true,
      "sortOrder": 3,
      "description": "Task has been finished successfully"
    },
    {
      "value": "Waiting on someone else",
      "label": "Waiting on someone else",
      "isDefault": false,
      "isActive": true,
      "sortOrder": 4,
      "description": "Task is blocked waiting for others"
    },
    {
      "value": "Deferred",
      "label": "Deferred",
      "isDefault": false,
      "isActive": true,
      "sortOrder": 5,
      "description": "Task has been postponed to a later date"
    }
  ],
  "totalCount": 5
}
```

## Common Use Cases

1. **Task Creation Forms**: Populate status dropdown lists in task creation interfaces
2. **Task Management**: Provide status options for task updates
3. **Reporting**: Generate reports showing task distribution by status
4. **Workflow Configuration**: Configure automated task status transitions
5. **User Interface**: Build dynamic task management interfaces
6. **Data Validation**: Validate task status values before updates

## Integration Patterns

### Form Population
Use the response to populate dropdown lists or selection controls in task management forms.

### Status Validation
Validate task status values against the returned list before creating or updating tasks.

### Reporting Categories
Group tasks by status categories (Active, Completed, Deferred) for reporting purposes.

### Workflow Rules
Use status values to configure automated workflow rules and transitions.

## Custom Task Statuses

Organizations may have custom task statuses beyond the standard ones:

### Examples of Custom Statuses
- "Under Review" - Task completed but awaiting approval
- "On Hold" - Task temporarily suspended
- "Escalated" - Task requiring management attention
- "Rework Required" - Task needs to be redone

### Custom Status Properties
Custom statuses will have the same structure as standard statuses but with organization-specific values and descriptions.

## Best Practices

1. **Cache Results**: Cache task status values as they change infrequently
2. **Default Handling**: Use the default status for new task creation
3. **Status Validation**: Always validate status values before task updates
4. **User Experience**: Present statuses in logical order (use sortOrder)
5. **Localization**: Use appropriate language codes for international users
6. **Active Only**: Typically exclude inactive statuses from user interfaces

## Error Handling

### Common Issues
- **Permission Denied**: User lacks access to task status metadata
- **Invalid Language**: Specified language code not supported
- **System Error**: Salesforce metadata service unavailable

### Error Prevention
- Ensure user has appropriate metadata access permissions
- Use standard language codes (ISO 639-1)
- Implement fallback to default language if localization fails

## Related Requests

- [Task Change Alert](pages/TaskChangeAlert.md) - Monitor task status changes
- [Get Opportunities](pages/GetOpportunities.md) - Related opportunity management
- [Get All Quote Status](pages/GetAllQuoteStatus.md) - Similar status retrieval for quotes

## See Also

- [Task Entity](pages/entities/Task.md)
- [User Entity](pages/entities/User.md)
- [Account Entity](pages/entities/Account.md)
