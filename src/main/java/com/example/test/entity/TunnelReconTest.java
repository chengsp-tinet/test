package com.example.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author chengsp
 * @date 2020/6/20 11:32
 */
/**
    * 通道对账表
    */
@Data
@TableName(value = "sp_batch_tunnel_recon_test")
public class TunnelReconTest implements Serializable {
    /**
     * 主键编号，不为空，主键
     */
    @TableId(value = "PID", type = IdType.AUTO)
    private Long pid;

    /**
     * 报文标识号
     */
    @TableField(value = "TXN_ID")
    private String txnId;

    /**
     * 业务流水号
     */
    @TableField(value = "MSG_ID")
    private String msgId;

    /**
     * 请求流水号
     */
    @TableField(value = "REQ_ID")
    private String reqId;

    /**
     * 订单号
     */
    @TableField(value = "ORDER_ID")
    private String orderId;

    /**
     * 端到端标识
     */
    @TableField(value = "END_TO_END_NO")
    private String endToEndNo;

    /**
     * 通道编码
     */
    @TableField(value = "TUNNEL_ID")
    private String tunnelId;

    /**
     * 发起行号
     */
    @TableField(value = "SNDR_NO")
    private String sndrNo;

    /**
     * 交易金额
     */
    @TableField(value = "AMOUNT")
    private BigDecimal amount;

    /**
     * 币种
     */
    @TableField(value = "CCY")
    private String ccy;

    /**
     * 业务种类
     */
    @TableField(value = "BIZ_TYPE")
    private String bizType;

    /**
     * 报文类型
     */
    @TableField(value = "MSG_TYPE")
    private String msgType;

    /**
     * 交易类型
     */
    @TableField(value = "TXN_TYPE")
    private String txnType;

    /**
     * 交易时间
     */
    @TableField(value = "TXN_TIME")
    private String txnTime;

    /**
     * 交易状态
     */
    @TableField(value = "TXN_STATUS")
    private String txnStatus;

    /**
     * 对账状态
     */
    @TableField(value = "RECON_STATUS")
    private String reconStatus;

    /**
     * 平台批量日期
     */
    @TableField(value = "BATCH_DATE")
    private String batchDate;

    /**
     * 平台对账日期
     */
    @TableField(value = "RECON_DATE")
    private String reconDate;

    /**
     * 商户编号
     */
    @TableField(value = "MERCH_ID")
    private String merchId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "MODIFY_TIME")
    private Date modifyTime;

    @TableField(value = "SPLIT_GROUP_ID")
    private String splitGroupId;

    private static final long serialVersionUID = 1L;
}