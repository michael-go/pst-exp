package org.pipoware.pst.exp;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import static org.pipoware.pst.exp.PropertyDataType.PtypBinary;
import static org.pipoware.pst.exp.PropertyDataType.PtypBoolean;
import static org.pipoware.pst.exp.PropertyDataType.PtypInteger32;
import static org.pipoware.pst.exp.PropertyDataType.PtypObject;
import static org.pipoware.pst.exp.PropertyDataType.PtypString;
import static org.pipoware.pst.exp.PropertyDataType.PtypString8;
import static org.pipoware.pst.exp.PropertyDataType.PtypTime;

/**
 *
 * @author Franck Arnulfo
 */
public enum PropertyTag {

  /*
   * MS-PST v5.2 / 2.1.2 Properties
   */
  PidTagNameidBucketCount((short) 0x0001, PtypInteger32),
  PidTagNameidStreamGuid((short) 0x0002, PtypBinary),
  PidTagNameidStreamEntry((short) 0x0003, PtypBinary),
  PidTagNameidStreamString((short) 0x0004, PtypBinary),
  PidTagNameidBucketBase((short) 0x1000, PtypBinary),
  PidTagItemTemporaryFlags((short) 0x1097, PtypInteger32),
  PidTagPstBestBodyProptag((short) 0x661D, PtypInteger32),
  PidTagPstHiddenCount((short) 0x6635, PtypInteger32),
  PidTagPstHiddenUnread((short) 0x6636, PtypInteger32),
  PidTagPstIpmsubTreeDescendant((short) 0x6705, PtypBoolean),
  PidTagPstSubTreeContainer((short) 0x6772, PtypInteger32),
  PidTagLtpParentNid((short) 0x67F1, PtypInteger32),
  PidTagLtpRowId((short) 0x67F2, PtypInteger32),
  PidTagLtpRowVer((short) 0x67F3, PtypInteger32),
  PidTagPstPassword((short) 0x67FF, PtypInteger32),
  PidTagMapiFormComposeCommand((short) 0x682F, PtypString),
  
  /*
   * MS-PST v5.2 / 2.4.3.1 Minimum Set of Required Properties
   */
  PidTagIpmSubTreeEntryId((short) 0x35E0, PtypBinary),

  /*
   * [MS-OXPROPS] v18.0 / 2.X
   */
  PidTagSubject((short) 0x0037, PtypString),
  PidTagMessageDeliveryTime((short) 0x0E06, PtypTime),
  PidTagMessageFlags((short) 0x0E07, PtypInteger32),
  PidTagCreationTime((short) 0x3007, PtypTime),
  PidTagLastModificationTime((short) 0x3008, PtypTime),
  PidTagAttachEncoding((short) 0x3702, PtypBinary),
  PidTagAttachExtension((short) 0x3703, PtypString),
  PidTagAttachFilename((short) 0x3704, PtypString),
  PidTagAttachLongFilename((short) 0x3707, PtypString),
  PidTagAttachRendering((short) 0x3709, PtypBinary),
  PidTagAttachDataBinary((short) 0x3701, PtypBinary),
  PidTagAttachDataObject((short) 0x3701, PtypObject),
  PidTagAttachSize((short) 0x0E20, PtypInteger32),
  PidTagAttachMethod((short) 0x3705, PtypInteger32),
  PidTagRenderingPosition((short) 0x370B, PtypInteger32),
  PidTagDisplayName((short) 0x3001, PtypString, PtypString8),
  PidTagSubfolders((short) 0x360A, PtypBoolean),
  PidTagContentCount((short) 0x3602, PtypInteger32),
  PidTagContentUnreadCount((short) 0x3603, PtypInteger32);

  private final short propertyIdentifier;
  private final List<PropertyDataType> propertyDataTypes = new ArrayList<>();

  private PropertyTag(short propertyIdentifier, PropertyDataType propertyDataType) {
    this.propertyIdentifier = propertyIdentifier;
    this.propertyDataTypes.add(propertyDataType);
  }

  private PropertyTag(short propertyIdentifier, PropertyDataType propertyDataType, PropertyDataType alternativePropertyDataType) {
    this.propertyIdentifier = propertyIdentifier;
    this.propertyDataTypes.add(propertyDataType);
    this.propertyDataTypes.add(alternativePropertyDataType);
  }

  public short getPropertyIdentifier() {
    return propertyIdentifier;
  }

  public List<PropertyDataType> getPropertyDataTypes() {
    return propertyDataTypes;
  }

  public static PropertyTag getPropertyTagFromIdentifier(short propertyIdentifier, PropertyDataType propertyDataType) {
    for (PropertyTag tag : EnumSet.allOf(PropertyTag.class)) {
      if (tag.getPropertyIdentifier() == propertyIdentifier
        && (tag.propertyDataTypes.contains(propertyDataType))) {
        return tag;
      }
    }
    return null;
  }
};
