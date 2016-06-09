package changan;

/**
 * Created by Beeant on 2016/4/29.
 */
public enum PackageEnum {
    pageBounds("com.github.miemiedev.mybatis.paginator.domain.PageBounds"),
    pageList("com.github.miemiedev.mybatis.paginator.domain.PageList"),
    param("org.apache.ibatis.annotations.Param"),
    baseMybatis("com.changan.anywhere.persistence.repository.mybatis.IBaseMybatisDao"),
    mybaisRepository("com.changan.anywhere.persistence.repository.mybatis.MyBatisRepository");

    private String packge;

    /**
     * Getter for property 'packge'.
     *
     * @return Value for property 'packge'.
     */
    public String getPackge() {
        return packge;
    }

    PackageEnum(String packge) {
        this.packge = packge;
    }
}
