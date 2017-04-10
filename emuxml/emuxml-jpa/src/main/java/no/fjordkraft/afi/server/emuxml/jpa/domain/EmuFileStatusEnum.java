package no.fjordkraft.afi.server.emuxml.jpa.domain;

public enum EmuFileStatusEnum {
    created("In ISCU source directory, but not scanned"),
    scanning("Scanning in progress"),
    ok("Scanned OK"),
    error("Scanned with errors");

    String description;

    EmuFileStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}