package iu.e510.message.board.cluster.data.payloads;

import iu.e510.message.board.cluster.data.DataManager;
import iu.e510.message.board.cluster.data.beans.BaseBean;
import iu.e510.message.board.tom.common.NonBlockingCall;
import iu.e510.message.board.tom.common.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDataPayload extends Payload<BaseBean> implements NonBlockingCall {
    private static final Logger logger = LoggerFactory.getLogger(ClientDataPayload.class);

    public ClientDataPayload(BaseBean content) {
        super(content);
    }

    @Override
    public void process(DataManager dataManager) {
        logger.info("Client data received!");

        BaseBean bean = getContent();
        // add data to topics set and zk
        try {
            dataManager.addTopicData(bean.getTopic());
        } catch (Exception e) {
            throw new RuntimeException("unable to process CLIENT DATA payload", e);
        }

        // process payload data bean and store the data in the db
        bean.processBean(dataManager.getDatabase());
    }

    @Override
    public String toString() {
        return "ClientDataPayload{" +
                "content=" + getContent() +
                '}';
    }
}
