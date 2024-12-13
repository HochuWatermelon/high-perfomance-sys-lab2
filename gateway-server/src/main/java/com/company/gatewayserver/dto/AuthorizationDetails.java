package com.company.gatewayserver.dto;

import java.util.List;
import java.util.UUID;

public record AuthorizationDetails(UUID id,
                                   String fullName,
                                   List<String> roles) {


}
