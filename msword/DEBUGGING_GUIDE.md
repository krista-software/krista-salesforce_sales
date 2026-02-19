# MS Word Watermark Fix - Debugging Guide

## 🔍 What the Logs Will Show

When you run the "Fill The Docx Template" request, you'll see detailed console output showing:

### 1. Entry Point Detection
```
╔═══════════════════════════════════════════════════════╗
║  MS WORD TEMPLATE PROCESSING - ENTRY POINT           ║
╚═══════════════════════════════════════════════════════╝
📄 Template File: your-template.docx
🔍 Watermark Detected: true/false
🔍 Has Rich Text Content: true/false
➡️  DECISION: Use fillWithDocx4jProcessing (WATERMARK PATH)
```

### 2. Routing Decision
```
═══════════════════════════════════════════════════════
📄 fillWithDocx4jProcessing - ROUTING DECISION
   Template: your-template.docx
   Has HTML Content: true/false
   Fields to replace: [$PropertyLegalDescription, $PropAddress, ...]
   ➡️  ROUTE: fillWithDirectZipManipulation (SIMRAN'S FIX)
       OR
   ➡️  ROUTE: fillWithDocx4jDomProcessing (OUR FIX)
═══════════════════════════════════════════════════════
```

### 3A. If Using Simran's Fix (Plain Text)
```
🟢 SIMRAN'S FIX: fillWithDirectZipManipulation
   Processing plain text replacements via ZIP manipulation
   📝 Will replace: $PropertyLegalDescription → 123 Main Street...
   📝 Will replace: $PropAddress → Anytown, USA...
```

### 3B. If Using Our Fix (HTML Content)
```
🔵 OUR FIX: fillWithDocx4jDomProcessing
   Processing HTML content via DOM manipulation
   Fields to process: [$PropertyLegalDescription, $PropAddress, ...]
   📄 Processing main document part...
```

### 4. Watermark Paragraph Processing (Our Fix Only)
```
🔵 WATERMARK PARAGRAPH DETECTED
   Original text: $PropertyLegalDescription $PropAddress
   Available fields: [$PropertyLegalDescription, $PropAddress, $OtherField]
   ✅ Replacing: $PropertyLegalDescription → 123 Main Street...
   Text after replacement #1: 123 Main Street $PropAddress
   ✅ Replacing: $PropAddress → Anytown, USA...
   Text after replacement #2: 123 Main Street Anytown, USA
   Total replacements made: 2
```

### 5. Non-Watermark Paragraph (Our Fix Only)
```
🟢 NON-WATERMARK: Replacing $FieldName in: Some paragraph text...
```

---

## 🎯 What to Look For

### ✅ GOOD - Working Correctly
- Watermark detected: `true`
- Route shows: `fillWithDirectZipManipulation` OR `fillWithDocx4jDomProcessing`
- For watermark paragraphs: `Total replacements made: 2` (or however many fields are in that paragraph)
- All placeholders shown as replaced

### ❌ BAD - Issue Detected

**Issue 1: Watermark Not Detected**
```
🔍 Watermark Detected: false
➡️  DECISION: Use XWPFDocManager (STANDARD PATH)
```
→ **Problem**: Watermark detection is failing
→ **Fix**: Check watermark detection logic

**Issue 2: Wrong Route Taken**
```
Has HTML Content: false
➡️  ROUTE: fillWithDirectZipManipulation (SIMRAN'S FIX)
```
But you expected HTML content to be processed
→ **Problem**: Fields are not being detected as HTML
→ **Fix**: Check field type detection

**Issue 3: Only First Placeholder Replaced**
```
🔵 WATERMARK PARAGRAPH DETECTED
   Original text: $PropertyLegalDescription $PropAddress
   ✅ Replacing: $PropertyLegalDescription → 123 Main Street...
   Total replacements made: 1
```
→ **Problem**: Loop is breaking early (our fix not working)
→ **Fix**: Check the replaceVarsInPart logic

**Issue 4: No Watermark Paragraphs Found**
```
🔵 OUR FIX: fillWithDocx4jDomProcessing
   Processing HTML content via DOM manipulation
   📄 Processing main document part...
```
But no "WATERMARK PARAGRAPH DETECTED" messages
→ **Problem**: Watermark detection in paragraphs is failing
→ **Fix**: Check isWatermarkParagraph() method

---

## 📦 Deployment Instructions

1. **Deploy the JAR**:
   ```
   Location: msword/build/libs/ms-word-2.0.19.jar
   ```

2. **Copy to Krista extensions directory**

3. **Restart Krista Server**

4. **Run the test** in Patten Title workspace:
   - "99. Replay Email Order by Message ID"
   - Use a watermarked DOCX template with multiple fields on same line

5. **Check the console logs** for the output patterns above

6. **Share the logs** if issue persists - they will show exactly where the problem is

---

## 🔧 Version Info

- **Extension Version**: 2.0.19 (with debug logging)
- **Build Date**: 2026-02-17
- **Changes**: Added comprehensive debug logging to track template processing flow

