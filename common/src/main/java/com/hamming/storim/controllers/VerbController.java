package com.hamming.storim.controllers;

import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.VerbListener;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.ExecVerbDTO;
import com.hamming.storim.model.dto.protocol.ExecVerbResultDTO;
import com.hamming.storim.model.dto.protocol.GetVerbResultDTO;
import com.hamming.storim.model.dto.protocol.VerbDeletedDTO;
import com.hamming.storim.net.NetCommandReceiver;

import java.util.ArrayList;
import java.util.List;

public class VerbController {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private List<VerbListener> verbListeners;
    private UserController userController;


    public VerbController(Controllers controllers) {
        this.connectionController = controllers.getConnectionController();
        this.userController = controllers.getUserController();
        protocolHandler = new ProtocolHandler();
        verbListeners = new ArrayList<VerbListener>();
        connectionController.registerReceiver(GetVerbResultDTO.class, new NetCommandReceiver<GetVerbResultDTO>(){
            @Override
            public void receiveDTO(GetVerbResultDTO dto) {
                handleGetVerbResult(dto);
            }
        });
        connectionController.registerReceiver(ExecVerbResultDTO.class, new NetCommandReceiver<ExecVerbResultDTO>(){
            @Override
            public void receiveDTO(ExecVerbResultDTO dto) {
                handleExecVerbResult(dto);
            }
        });
        connectionController.registerReceiver(VerbDeletedDTO.class, new NetCommandReceiver<VerbDeletedDTO>(){
            @Override
            public void receiveDTO(VerbDeletedDTO dto) {
                handleVerbDeletedDTO(dto);
            }
        });
    }

    private void handleVerbDeletedDTO(VerbDeletedDTO dto) {
        for (VerbListener l : verbListeners) {
            l.verbDeleted(dto.getVerbID());
        }

    }

    private void handleExecVerbResult(ExecVerbResultDTO dto) {
        if (dto.isSuccess() ) {
            for (VerbListener l : verbListeners) {
                l.verbExecuted(dto);
            }
        }
    }

    private void handleGetVerbResult(GetVerbResultDTO dto) {
        if (dto.isSuccess() ) {
            VerbDto verbDto = dto.getVerb();
            for (VerbListener l : verbListeners) {
                l.verbReceived(verbDto);
            }
        }
    }

    public void addVerbListener(VerbListener l) {
        verbListeners.add(l);
    }



    public void executeVerb(VerbDto command, String input) {
        ExecVerbDTO dto = protocolHandler.getExecVerbDTO(command.getId(), input);
        connectionController.send(dto);
    }

    public void deleteVerb(VerbDto verb) {
        connectionController.send(protocolHandler.getDeleteVerbDTO(verb.getId()));
    }

    public void addVerb( String name, String shortName, String toCaller, String toLocation) {
        connectionController.send(protocolHandler.getAddVerbDTO(name, shortName, toCaller, toLocation));
    }

    public void updateVerb(Long id, String name, String shortName, String toCaller, String toLocation) {
        connectionController.send(protocolHandler.getUpdateVerbDTO(id, name, shortName, toCaller, toLocation));
    }
}
