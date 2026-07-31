package com.nectavox.nxtpa.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PlayerData {

    UUID uuid;
    String name;

    List<UUID> ignoredPlayers;

    boolean tpAuto;
    boolean tpaConfirm;
    boolean tpaRequests;
    boolean tpaHereRequests;
    boolean tpaAccept;
    boolean backConfirm;

}
