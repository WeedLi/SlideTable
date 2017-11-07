package com.leo.monthtable.monthtablelibrary.adapter;

import com.leo.monthtable.monthtablelibrary.bean.TableContentBean;
import com.leo.monthtable.monthtablelibrary.bean.TableFirstColumnBean;
import com.leo.monthtable.monthtablelibrary.bean.TableFirstRowBean;

import java.util.List;

/**
 * 作者：Leo on 2017/11/2 11:09
 * <p>
 * 描述：
 */

public interface ITableAdapter {

    //    column 列 row 行

//    /**
//     *  获取行数 除掉第一行
//     * @return
//     */
//    int getRowCount();

    /**
     *  获取列数 除掉第一列
     * @return
     */
    int getColumnCount();

    /**
     *  获取第一行的高度
     * @return
     */
    int getFirstRowHeight();

    /**
     * 获取第一列的宽度
     * @return
     */
    int getFirstColumnWidth();

    /**
     *  获取其他的格子的高度
     * @return
     */
    int getOtherRowHeight();

    /**
     *  获取其他格子的宽度
     * @return
     */
    int getOtherColumnWidth();

    /**
     *  获取列与列间的距离
     * @return
     */
    int getColumnPadding();


    int getFirstRowLayoutId();

    List<TableFirstRowBean> getFirstRowData();

    void bindFirstRowItemHolder(BaseViewHolder holder, int position, TableFirstRowBean tableRowBean);

    int getFirstColumnLayoutId();

    List<TableFirstColumnBean> getFirstColumnData();

    void bindFirstColumnItemHolder(BaseViewHolder holder, int position, TableFirstColumnBean tableColumnBean);

    int getContentLayoutId();

    List<List<TableContentBean>> getContentData();

    void bindContentItemHolder(LinearLayoutViewHolder holder, int position, TableContentBean tableContentBean, int parentPosition);

}
