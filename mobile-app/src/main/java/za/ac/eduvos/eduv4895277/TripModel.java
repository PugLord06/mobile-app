package za.ac.eduvos.eduv4895277;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripModel implements Serializable {
    public String destination;
    public String startDate;
    public String endDate;
    public String notes;
    public double customExpense;
    public double mealsExpense;
    public List<String> activities = new ArrayList<>();
    public double subtotal;
    public double discount;
    public double total;
} 