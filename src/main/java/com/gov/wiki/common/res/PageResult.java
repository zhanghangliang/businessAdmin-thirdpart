package com.gov.wiki.common.res;

import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.SortDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@ApiModel(value = "PageInfo", description = "分页对象")
@ToString
public class PageResult<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(name = "currentPage", value = "当前页")
    private Integer currentPage;

    /**
     * 每页显示条数
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示条数")
    private Integer pageSize;

    /**
     * 总页数
     */
    @ApiModelProperty(name = "totalPages", value = "总页数")
    private Integer totalPages;

    /**
     * 总数
     */
    @ApiModelProperty(name = "total", value = "总条数")
    private long total;

    /**
     * 结果集
     */
    @ApiModelProperty(name = "dataList", value = "结果集")
    private List<T> dataList;

    @ApiModelProperty(name = "sortList", value = "排序字段对象")
    private SortDto[] sortList;

    public Integer getCurrentPage() {
        if(currentPage == null){
            currentPage = PageableTools.initPage;
        }
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        if(pageSize == null){
            pageSize = PageableTools.pageSize;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }



    public SortDto[] getSortList() {
        return sortList;
    }

    public void setSortList(SortDto[] sortList) {
        this.sortList = sortList;
    }

    /**
     * 	把旧的分页信息复制到新的分页数据中，不包含复制分页的data
     * @param <T>
     * @param oldPage
     * @return
     */
    public static <T> PageResult<T> copyPage(PageResult oldPage){
        PageResult<T> result = new PageResult<T>();
        result.currentPage = oldPage.currentPage;
        result.pageSize = oldPage.pageSize;
        result.total = oldPage.pageSize;
        result.totalPages = oldPage.totalPages;
        return result;
    }

    public PageResult() {

    }

    public PageResult(Page page){
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
