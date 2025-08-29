package za.ac.eduvos.eduv4895277;

import java.io.Serializable;

public class MemoryModel implements Serializable {
    public long id;
    public String photoUri; // content URI string
    public String audioUri; // content URI string (optional)
    public String notes;
    public long createdAt;
} 