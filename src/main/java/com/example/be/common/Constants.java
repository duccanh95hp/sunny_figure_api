package com.example.be.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Constants {
    public static final String NOT_FOUND = "Không tồn tại trong hệ thống";
    public static final String SUCCESS = "SUCCESS";
    public static final String EMPTY = "Không có dữ liệu";
    public static final String FAILURE = "thất bại";
    public static final String NEW_ORDER = "tạo mới";
    public static final String APPROVE = "xác nhận";
    public static final String TRANSPORT = "vận chuyển";
    public static final String COMPLETE = "hoàn thành";
    public static final String REJECT = "từ chối";
    public static final String ACTIVE = "hoạt động";
    public static final String IN_ACTIVE = "không hoạt động";
    public static final String WAIT_CONFIRM = "chờ xác nhận";
    public static final String CANCEL = "hủy";
    public static final String PURCHASE_EXCEEDS_AVAILABLE = "Số lượng mua vượt qua số lượng hiện có";
    public static final String NO_SELF_INTRODUCTION = "Không được tự giới thiệu";
    public static final String AFFILIATE_CODE_NOT_FOUND = "Mã giới thiệu không tồn tại";
    public static final String COMPLETED_NOT_UPDATE = "Đơn hàng hoàn thành không được thay đổi";
    public static final String NOT_FORMAT_DATE = "Không đúng định dang Ngày tháng";
}