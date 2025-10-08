# Event Entity

## Overview

The Event entity represents scheduled meetings, calls, and appointments in Salesforce Sales. Events help sales teams manage their calendar, track customer interactions, and ensure important meetings are scheduled and attended.

## Entity Details

- **Entity Name**: Event
- **Primary Key**: Id
- **Searchable Fields**: Subject, Lead Id
- **Display Fields**: Subject

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Event record |
| Subject | Text | Yes | Subject or title of the event (searchable, used for display) |
| Owner Id | Text | No | ID of the user who owns this event |
| Location | Text | No | Location where the event takes place |

### Scheduling

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Start Date Time | Date | Yes | Start date and time of the event |
| End Date Time | Date | No | End date and time of the event |
| Duration In Minutes | Number | No | Duration of the event in minutes |

### Related Records

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Account Id | Text | No | ID of the associated account |
| Lead Id | Text | No | ID of the associated lead (searchable, used for display) |
| What Id | Text | No | ID of the related record (Account, Opportunity, etc.) |
| Who Id | Text | No | ID of the related person (Contact, Lead) |

### Event Properties

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Is Private | Switch | No | Whether the event is private |
| Is Group Event | Switch | No | Whether this is a group event |
| Is All Day Event | Switch | No | Whether this is an all-day event |
| Show As | Text | No | How to show the time (Busy, Free, etc.) |

### System Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Created Date | Date | No | Date and time when the event was created |
| Last Modified Date | Date | No | Date and time of last modification |
| Description | Paragraph | No | Additional details about the event |

## Event Types (Common)

- **Meeting**: Face-to-face or virtual meetings with customers
- **Call**: Scheduled phone calls with prospects or customers
- **Demo**: Product demonstrations and presentations
- **Training**: Training sessions or workshops
- **Conference**: Industry conferences and events
- **Follow-up**: Follow-up meetings after initial contact

## Usage in Catalog Requests

Events are typically used in:
- Calendar management and scheduling
- Activity tracking and reporting
- Customer interaction history
- Sales process documentation

## Event Lifecycle

### Planning
1. **Identify Need**: Determine need for meeting or call
2. **Schedule Event**: Set date, time, and location
3. **Invite Participants**: Add attendees and send invitations
4. **Prepare Materials**: Gather necessary documents and information

### Execution
1. **Pre-Event**: Confirm attendance and logistics
2. **Conduct Event**: Hold the meeting, call, or appointment
3. **Document Outcomes**: Record results and next steps
4. **Follow-up**: Schedule follow-up activities if needed

## Relationships

- **Many-to-One with User (Owner)**: Each event has one owner
- **Many-to-One with Account**: Events can be associated with accounts
- **Many-to-One with Lead**: Events can be associated with leads
- **Many-to-One with Contact**: Events can be associated with contacts
- **Many-to-One with Opportunity**: Events can be associated with opportunities

## Best Practices

1. **Clear Subjects**: Use descriptive subjects that explain the purpose
2. **Accurate Timing**: Set realistic start and end times
3. **Location Details**: Provide clear location information
4. **Record Association**: Link events to relevant accounts, leads, or opportunities
5. **Follow-up Planning**: Schedule follow-up events when appropriate
6. **Documentation**: Add descriptions with meeting outcomes and next steps

## Common Use Cases

1. **Customer Meetings**: Schedule meetings with prospects and customers
2. **Sales Calls**: Plan and track sales-related phone calls
3. **Product Demos**: Schedule product demonstrations
4. **Follow-up Activities**: Plan follow-up meetings after initial contact
5. **Team Meetings**: Coordinate internal team meetings
6. **Conference Tracking**: Track attendance at industry events

## Example Usage

```json
{
  "id": "00U5w00000ABC123",
  "subject": "Product Demo - Acme Corp",
  "startDateTime": 1640995200000,
  "endDateTime": 1640998800000,
  "location": "Customer Office - Conference Room A",
  "accountId": "0015w00000ABC123",
  "ownerId": "0055w00000ABC123",
  "isPrivate": false,
  "isGroupEvent": false,
  "description": "Demonstrate enterprise software features and discuss implementation timeline"
}
```

## Calendar Integration

Events integrate with calendar systems to:
- Sync with Outlook, Google Calendar, etc.
- Send meeting invitations
- Provide calendar visibility
- Enable mobile access
- Support recurring events

## See Also

- [Account Entity](pages/entities/Account.md)
- [Lead Entity](pages/entities/Lead.md)
- [User Entity](pages/entities/User.md)
- [Task Entity](pages/entities/Task.md)
