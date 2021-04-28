package net.sejongtelecom.backendapi.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CdmasterDto {

  @JsonProperty(value = "fundid")
  private String fundid;

  @JsonProperty(value = "initcnt")
  private int initcnt;
}
