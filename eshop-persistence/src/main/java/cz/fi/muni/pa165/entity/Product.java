package cz.fi.muni.pa165.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "ESHOP_PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique=true)
    private String name;

    private Color color;

    private LocalDate addedDate;

    public Product(Long categoryId) {
        this.id = categoryId;
    }
    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() { return color; }

    public void setColor(Color color) {
        this.color = color;
    }

    public LocalDate getAddedDate() { return addedDate; }

    public void setAddedDate(LocalDate localDate) {
        this.addedDate = localDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Product))
            return false;

        Product product = (Product) o;
        return name.equals(product.getName()) &&
                color == product.getColor() &&
                Objects.equals(addedDate, product.getAddedDate());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((name == null) ? 0 : name.hashCode())
                + ((color == null) ? 0 : color.hashCode())
                + ((addedDate == null) ? 0 : addedDate.hashCode());
        return result;
    }
}
