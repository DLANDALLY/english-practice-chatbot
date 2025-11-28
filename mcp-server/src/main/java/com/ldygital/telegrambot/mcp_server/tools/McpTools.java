package com.ldygital.telegrambot.mcp_server.tools;

import com.ldygital.telegrambot.mcp_server.entities.Employe;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;

import java.util.List;

public class McpTools {
    @McpTool(name = "getEmployee",
            description = "Get information about a given employee")
    public Employe getEmployee(@McpArg(description = "The employee name") String name) {
        return new Employe(name, 12300, 4);
    }

    @McpTool(description = "Get All Employees")
    public List<Employe> getAllEmployees() {
        return List.of(
                new Employe("Hassan", 12300, 4),
                new Employe("Mohamed", 34000, 1),
                new Employe("Imane", 23000, 10)
        );
    }
}

