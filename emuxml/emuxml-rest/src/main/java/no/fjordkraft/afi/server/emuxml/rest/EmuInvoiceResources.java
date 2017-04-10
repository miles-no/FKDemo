package no.fjordkraft.afi.server.emuxml.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.rest.RestUtil;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceRequest;
import no.fjordkraft.afi.server.emuxml.services.EmuInvoiceFetchService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Slf4j
@Component
@Api(value = EmuInvoiceResources.PATH, description = "Operations about fetching emu invoices")
@Path(EmuInvoiceResources.PATH)
public class EmuInvoiceResources {

    public static final String PATH = "/emuinvoice";

    @Autowired
    private EmuInvoiceFetchService emuInvoiceFetchService;

    @GET
    @ApiOperation(value = "List EMU invoice")
    @Path("/list/{accountNo}")
    @Produces("application/json")
    public Response list(
            @ApiParam(value = "accountNo", required = true) @PathParam("accountNo") final String accountNo,
            @ApiParam(value = "From-date.  Format: YYYY-MM-DD", required = false) @QueryParam("fromDate") final String fromDateAsStr,
            @ApiParam(value = "To-date.  Format: YYYY-MM-DD", required = false) @QueryParam("toDate") final String toDateAsStr,
            @ApiParam(value = "pagenumber. ", required = false) @QueryParam("pagenumber") final Integer pagenumber,
            @ApiParam(value = "pagesize. ", required = false, defaultValue = "20") @QueryParam("pagesize") final Integer pagesize
    ) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        return RestUtil.createNoCacheResult(new EmuInvoiceJsonBuilder().toJSON(
                emuInvoiceFetchService.filterByRequest(EmuInvoiceRequest.withPeriod(
                        accountNo,
                        fromDateAsStr != null ? format.parseDateTime(fromDateAsStr) : null,
                        toDateAsStr != null ? format.parseDateTime(toDateAsStr) : null,
                        new PageRequest((pagenumber != null ? pagenumber - 1 : 0), (pagesize != null ? pagesize.intValue() : 20))))).toString());
    }

}
