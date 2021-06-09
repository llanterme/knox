package za.co.knox.restservice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_history")
public class PurchaseHistoryEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_history_id")
    private int purchaseHistoryId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "order_ref")
    private String orderRef;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "voucher_pin")
    private String voucherPin;

    @Column(name = "time_of_purchase")
    private String timeOfPurchase;

    @Column(name = "client_ref")
    private String clientRef;

    @Column(name = "voucher_description")
    private String voucherDescription;

    @Column(name = "purchase_date")
    private String puchaseDate;


}


