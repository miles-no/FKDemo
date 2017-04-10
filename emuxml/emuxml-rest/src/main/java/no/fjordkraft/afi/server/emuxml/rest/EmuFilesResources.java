package no.fjordkraft.afi.server.emuxml.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.rest.RestUtil;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;
import no.fjordkraft.afi.server.emuxml.services.EmuFileFetchService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Slf4j
@Component
@Api(value = EmuFilesResources.PATH, description = "Operations about fetching emu files")
@Path(EmuFilesResources.PATH)
public class EmuFilesResources {

    public static final String PATH = "/emufiles";

    @Autowired
    private EmuFileFetchService emuFileFetchService;

    @GET
    @ApiOperation(value = "List EMU-XML files")
    @Path("/list")
    @Produces("application/json")
    public Response list(
            @ApiParam(value = "From-date.  Format: YYYY-MM-DD", required = false) @QueryParam("fromDate") final String fromDateAsStr,
            @ApiParam(value = "To-date.  Format: YYYY-MM-DD", required = false) @QueryParam("toDate") final String toDateAsStr,
            @ApiParam(value = "allFiles", required = false) @QueryParam("allFiles") final Boolean allFiles,
            @ApiParam(value = "pagenumber. ", required = false) @QueryParam("pagenumber") final Integer pagenumber,
            @ApiParam(value = "pagesize. ", required = false, defaultValue = "250") @QueryParam("pagesize") final Integer pagesize
    ) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        return RestUtil.createNoCacheResult(new EmuFileJsonBuilder().toJSON(
                emuFileFetchService.filterByRequest(EmuFilesRequest.withPeriod(
                        fromDateAsStr != null ? format.parseDateTime(fromDateAsStr) : null,
                        toDateAsStr != null ? format.parseDateTime(toDateAsStr) : null,
                        allFiles,
                        true,
                        new PageRequest((pagenumber != null ? pagenumber : 0), (pagesize != null ? pagesize.intValue() : 250))))).toString());
    }

}
