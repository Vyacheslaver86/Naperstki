package com.naperstky.player;

import jakarta.persistence.*;

@Entity
@Table(name ="players")
public class Player {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;


    public Player() {

    }


    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private double balance = 1000.0;  // Игровой баланс
  @Column(name ="wins")
    private int wins;

  // Количество побед
    @Column(name ="losses")
    private int losses;















//          {
//              "moneyBank" : 1500;
//         }
//          {
//              "moneyBank" : 3000;
//          }


}
