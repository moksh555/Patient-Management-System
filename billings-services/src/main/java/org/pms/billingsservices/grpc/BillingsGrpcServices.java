package org.pms.billingsservices.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingsGrpcServices extends BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingsGrpcServices.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}", billingRequest.toString());
//        Business logic

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("1234")
                .setStatus("Active")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
