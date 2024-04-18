package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "computer_id")
    private Computer computer;

    @ManyToOne
    @JoinColumn(name = "peripheral_id")
    private Peripheral peripheral;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "location", length = 100)
    private String location;

    // Constructors, getters, and setters

    public Inventory() {
    }

    public Inventory(Computer computer, Peripheral peripheral, int quantity, String location) {
        this.computer = computer;
        this.peripheral = peripheral;
        this.quantity = quantity;
        this.location = location;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Peripheral getPeripheral() {
        return peripheral;
    }

    public void setPeripheral(Peripheral peripheral) {
        this.peripheral = peripheral;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
