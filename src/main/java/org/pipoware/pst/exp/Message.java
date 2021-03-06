package org.pipoware.pst.exp;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.pipoware.pst.exp.pages.NBTENTRY;

/**
 *
 * @author Franck Arnulfo
 */
public class Message {

  private final NDB ndb;
  private final int nid;
  private final PC messageObjectPC;
  private String subject = null;

  public Message(NDB aNDB, int aNID) throws IOException {
    this.ndb = aNDB;
    this.nid = aNID;
    messageObjectPC = ndb.getPCFromNID(nid);
    NBTENTRY nbtentry = ndb.getNBTENTRYFromNID(nid);
    if (nbtentry.bidSub != 0) {
      getRecipients(nbtentry);
    }
  }
  
  /**
   * Return Subject of message Special handling : MS-PST 2.5.3.1.1.1
   *
   * @return
   */
  public String getSubject() {
    if (subject == null) {
      initSubject();
    }

    return subject;
  }

  private void initSubject() {
    PCItem subjectItem = messageObjectPC.findPCItemByPropertyTag(PropertyTag.PidTagSubject);
    if (subjectItem == null) {
      subject = "";
    } else {
      subject = subjectItem.getString();
      if ((subject != null) && (subject.length() >= 2)) {
        if (subject.charAt(0) == 0x1) {
          subject = subject.substring(2);
        }
      }
    }
  }
  
  public Date getCreationTime() {
    return messageObjectPC.findPCItemByPropertyTag(PropertyTag.PidTagCreationTime).getTime();
  }
  
  public Date getLastModificationTime() {
    return messageObjectPC.findPCItemByPropertyTag(PropertyTag.PidTagLastModificationTime).getTime();
  }
  
  public Date getMessageDeliveryTime() {
    return messageObjectPC.findPCItemByPropertyTag(PropertyTag.PidTagMessageDeliveryTime).getTime();
  }

  private void getRecipients(NBTENTRY nbtentry) throws IOException {
    Block subBlock = ndb.getBlockFromBID(nbtentry.bidSub);
    Preconditions.checkNotNull(subBlock, "Sub Block Id 0x%s not found for NID 0x%s", Long.toHexString(nbtentry.bidSub), Integer.toHexString(nid));
    Preconditions.checkArgument(subBlock.blockType == Block.BlockType.SLBLOCK);
    
    for (SLENTRY slEntry : subBlock.rgentries_slentry) {
      if (new NID(slEntry.nid).nidType == NID.NID_TYPE_RECIPIENT_TABLE) {
        TC recipientTable = ndb.getTCFromSLENTRY(slEntry);
        //System.out.println(recipientTable.getRows());
        //recipientTable.displayRowMatrixData();
      }
    }
  }
  
  public boolean hasAttachment() {
    PCItem pcItem = messageObjectPC.findPCItemByPropertyTag(PropertyTag.PidTagMessageFlags);
    final int mfHasAttach = 0x00000010;
    return (pcItem.getInt() & mfHasAttach) != 0;
  }

  public void listAttachments() throws IOException {
    NBTENTRY nbtentry = ndb.getNBTENTRYFromNID(nid);
    Block subBlock = ndb.getBlockFromBID(nbtentry.bidSub);
    Preconditions.checkNotNull(subBlock, "Sub Block Id 0x%s not found for NID 0x%s", Long.toHexString(nbtentry.bidSub), Integer.toHexString(nid));
    Preconditions.checkArgument(subBlock.blockType == Block.BlockType.SLBLOCK);

    TC attachmentTable = null;
    for (SLENTRY slEntry : subBlock.rgentries_slentry) {
      if (new NID(slEntry.nid).nidType == NID.NID_TYPE_ATTACHMENT_TABLE) {
        attachmentTable = ndb.getTCFromSLENTRY(slEntry);
        break;
      }
    }
    
    if (attachmentTable != null) {
      attachmentTable.displayRowMatrixData();
      List<TCROWID> rows = attachmentTable.getRows();
      System.out.println(rows);
      for (TCROWID row : rows) {
        for (SLENTRY slEntry : subBlock.rgentries_slentry) {
          if (slEntry.nid == row.dwRowID) {
            PC attachmentPC = ndb.getPCFromNID(slEntry);
            System.out.println(attachmentPC);
            for (PCItem pcItem : attachmentPC.items) {
              System.out.println(pcItem);
            }
            break;
          }
        }

      }

    }
    
  }

  public void toStringMessageObjectPC() {
    for (PCItem item : messageObjectPC.items) {
      System.out.println(item.toString());
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("nid", "0x" + Integer.toHexString(nid))
      .add("subject", getSubject())
      .add("creationTime", getCreationTime())
      .add("lastModificationTime", getLastModificationTime())
      .add("messageDeliveryTime", getMessageDeliveryTime())
      .toString();
  }

}
