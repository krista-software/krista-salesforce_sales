# Latest Posts Entity

## Overview

The Latest Posts entity represents social media-style posts or feed items in Salesforce Sales. These posts enable collaboration and communication within the sales team, allowing users to share updates, insights, and files related to sales activities.

## Entity Details

- **Entity Name**: Latest Posts
- **Primary Key**: Actor Name
- **Display Fields**: Actor Name, Post Message

## Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Actor Name | Text | No | Name of the person who created the post (primary key) |
| Post Message | RichText | No | Content of the post message with rich text formatting |
| Files | File | No | Attached files (supports multiple file uploads) |

## Post Types

### Status Updates
- Progress updates on deals and opportunities
- Team announcements and news
- Achievement celebrations
- Goal and milestone updates

### Information Sharing
- Market insights and competitive intelligence
- Customer feedback and testimonials
- Best practices and tips
- Training materials and resources

### Collaboration
- Questions and requests for help
- Discussion topics and brainstorming
- Meeting notes and summaries
- Project updates and coordination

## Usage in Catalog Requests

Latest Posts may be used in:
- Team collaboration features
- Activity feeds and timelines
- Communication tracking
- Knowledge sharing systems

## Rich Text Features

The Post Message field supports rich text formatting including:
- **Bold** and *italic* text
- Bulleted and numbered lists
- Hyperlinks to external resources
- @mentions of team members
- #hashtags for categorization

## File Attachments

Posts can include multiple file attachments such as:
- Documents (PDF, Word, Excel)
- Images and screenshots
- Presentations and proposals
- Spreadsheets and reports
- Audio and video files

## Best Practices

1. **Clear Communication**: Write clear, concise post messages
2. **Relevant Content**: Share information relevant to sales activities
3. **Professional Tone**: Maintain professional communication standards
4. **File Organization**: Use descriptive file names for attachments
5. **Regular Updates**: Keep the team informed with regular updates
6. **Engagement**: Respond to and engage with team members' posts

## Common Use Cases

1. **Deal Updates**: Share progress on important opportunities
2. **Team Communication**: Coordinate team activities and meetings
3. **Knowledge Sharing**: Share insights and best practices
4. **Announcements**: Communicate important news and updates
5. **Collaboration**: Work together on proposals and presentations
6. **Recognition**: Celebrate team achievements and successes

## Example Usage

### Status Update Post
```json
{
  "actorName": "Sarah Johnson",
  "postMessage": "Great news! Just closed the <strong>Acme Corp Enterprise Deal</strong> for $150K! 🎉 Thanks to the team for all the support during the negotiation phase.",
  "files": []
}
```

### Information Sharing Post
```json
{
  "actorName": "Mike Chen", 
  "postMessage": "Found this great competitive analysis document that might help with the upcoming <em>TechStart</em> proposal. Key insights on pages 3-5.",
  "files": [
    {
      "name": "Competitive_Analysis_Q4_2023.pdf",
      "size": 2048576,
      "type": "application/pdf"
    }
  ]
}
```

### Collaboration Post
```json
{
  "actorName": "Jennifer Davis",
  "postMessage": "Working on the proposal for <strong>Global Manufacturing Inc</strong>. Does anyone have experience with their procurement process? Looking for insights on:<br/>• Typical decision timeline<br/>• Key stakeholders<br/>• Preferred contract terms",
  "files": []
}
```

## Feed Integration

Latest Posts integrate with Salesforce feeds to:
- Appear in record-specific feeds
- Show up in user activity streams
- Enable commenting and likes
- Support notifications and alerts
- Provide search and filtering capabilities

## Privacy and Security

- Posts visibility can be controlled by sharing settings
- Sensitive information should be shared carefully
- File attachments are subject to organization security policies
- Posts may be audited for compliance purposes

## See Also

- [User Entity](pages/entities/User.md)
- [Account Entity](pages/entities/Account.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
