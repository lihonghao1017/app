package com.jin91.preciousmetal.ui.price;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.limc.androidcharts.DateUtil;
import cn.limc.androidcharts.Utils;
import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.IChartData;
import cn.limc.androidcharts.entity.IStickEntity;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.ListChartData;
import cn.limc.androidcharts.entity.MACDEntity;
import cn.limc.androidcharts.entity.OHLCEntity;
import cn.limc.androidcharts.event.ITouchEventResponse;
import cn.limc.androidcharts.event.seletedIndexListener;
import cn.limc.androidcharts.view.BOLLMASlipCandleStickChart;
import cn.limc.androidcharts.view.GridChart;
import cn.limc.androidcharts.view.MACDChart;
import cn.limc.androidcharts.view.MASlipCandleStickChart;
import cn.limc.androidcharts.view.SlipLineChart;

import com.bloomplus.tradev2.BloomplusTradeV2;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.ListDialogEntity;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.ListViewDialog;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.ui.mine.LoginActivity;
import com.jin91.preciousmetal.util.MathUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.PriceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/4/30.
 */
public class PriceDetailActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener, PriceDetailView {

	public static final String TAG = "PriceDetailActivity";
	public static final int TIME_CHART = 0; // 分时图
	public static final int K_CHART = 1; // k 线图

	public static final int INDICATOR_MACD = 0;
	public static final int INDICATOR_BOLL = 1;
	public static final int INDICATOR_KDJ = 2;
	public static final int INDICATOR_RSI = 3;
	public static final int INDICATOR_SMA = 4;
	public static final int INDICATOR_EMA = 5;
	public static final int INDICATOR_ADX = 6;
	public static final int INDICATOR_ATR = 7;
	public static final int INDICATOR_DMI = 8;

	// ####################
	// sunan,20150128,缩放级别
	public int AxisCalc = 1;
	// ####################
	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	@ViewInject(R.id.rb_bottom_nav)
	RadioGroup rb_bottom_nav;
	@ViewInject(R.id.tv_price_time)
	TextView tvPriceTime;
	@ViewInject(R.id.tv_cycle)
	TextView tvCycle;
	@ViewInject(R.id.tv_indicator)
	TextView tvIndicator;
	@ViewInject(R.id.tv_price_buy)
	TextView tvPriceBuy;
	@ViewInject(R.id.tv_price_sell)
	TextView tvPriceSell;
	@ViewInject(R.id.tv_price_maxvalue)
	TextView tvPriceMaxvalue;
	@ViewInject(R.id.tv_price_minvalue)
	TextView tvPriceMinvalue;
	@ViewInject(R.id.tv_price_rise)
	TextView tvPriceRise;
	@ViewInject(R.id.tv_price_range)
	TextView tvPriceRange;
	@ViewInject(R.id.tv_open_price)
	TextView tvOpenPrice;
	@ViewInject(R.id.tv_yest_price)
	TextView tvYestPrice;
	@ViewInject(R.id.tv_price_shake)
	TextView tvPriceShake;
	@ViewInject(R.id.tv_tip_open)
	TextView tvTipOpen;
	@ViewInject(R.id.tv_tip_yest)
	TextView tvTipYest;
	@ViewInject(R.id.tv_tip_high)
	TextView tvTipHigh;
	@ViewInject(R.id.tv_tip_low)
	TextView tvTipLow;
	@ViewInject(R.id.tv_tip_ind5)
	TextView tvTipInd5;
	@ViewInject(R.id.tv_tip_ind10)
	TextView tvTipInd10;
	@ViewInject(R.id.tv_tip_ind20)
	TextView tvTipInd20;
	@ViewInject(R.id.tv_tip_ind60)
	TextView tvTipInd60;
	@ViewInject(R.id.bollmaslipcandlestickchart)
	BOLLMASlipCandleStickChart bollmaslipcandlestickchart;
	@ViewInject(R.id.tv_k_date)
	TextView tvKDate;
	@ViewInject(R.id.rl_price_k_content)
	RelativeLayout rlPriceKContent;
	@ViewInject(R.id.tv_tip_bottom1)
	TextView tvTipBottom1;
	@ViewInject(R.id.tv_tip_bottom2)
	TextView tvTipBottom2;
	@ViewInject(R.id.tv_tip_bottom3)
	TextView tvTipBottom3;
	@ViewInject(R.id.macdchart)
	MACDChart macdchart;
	@ViewInject(R.id.sliplinechart)
	SlipLineChart sliplinechart;
	@ViewInject(R.id.ll_top_tip1)
	LinearLayout llTopTip1;
	@ViewInject(R.id.ll_top_tip2)
	LinearLayout llTopTip2;
	@ViewInject(R.id.ll_bottom_tip)
	LinearLayout llBottomTip;
	@ViewInject(R.id.slipLineChart_time)
	SlipLineChart slipLineChartTime;
	@ViewInject(R.id.ll_time_top)
	LinearLayout llTimeTop;
	@ViewInject(R.id.tv_time_top_run)
	TextView tvTimeTopRun;
	@ViewInject(R.id.tv_time_date)
	TextView tvTimeDate;

	private ListViewDialog cycleDialog;
	private ListViewDialog indicatorDialog;
	private List<ListDialogEntity> cycleList;
	private List<ListDialogEntity> indicatorList;
	private int cyclePosition = 11;
	private int indicatorPosition = 0;
	private PriceDetailPre presenter;
	private String tradeCode; // 行情的代码
	private LoadingDialog loadingDialog;
	private PriceUtil priceUtil;
	private int type = K_CHART;// 0分时图 1K线图
	// private boolean priceIsSuccess; // 获取最新的行情是否成功
	// private boolean kIsSuccess;// 获取k线图是否成功
	List<IStickEntity> kChartList;
	Handler priceHandler = new Handler();
	Handler priceTimeHandler = new Handler();
	private boolean priceTimeVisible; // 分时图是否隐藏
	private boolean threadPriceTimeStart; // 线程分时图是否开始
	private boolean priceVisible; // 行情是否可见
	private boolean threadPriceStart; // 行情的线程是否开始
	private double closePrice;

	private int indicatorTop = INDICATOR_SMA;
	private int indicatorBottom = INDICATOR_MACD;
	private Price currentPrice;// 当前的价格
	private String titleName;

	/**
	 * @param context
	 * @param tradeCode
	 *            交易码
	 * @param titleName
	 *            交易的名称
	 */
	public static void actionLaunch(Context context, String tradeCode,
			String titleName, String sellPrice, String increase) {
		Intent i = new Intent(context, PriceDetailActivity.class);
		i.putExtra("tradeCode", tradeCode);
		i.putExtra("titleName", titleName);
		i.putExtra("sellPrice", sellPrice);
		i.putExtra("increase", increase);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_detail);
		ViewUtils.inject(this);
		initialize();
	}

	@Override
	public void initialize() {
		Intent intent = getIntent();
		priceUtil = new PriceUtil();
		tradeCode = intent.getStringExtra("tradeCode");
		titleName = intent.getStringExtra("titleName");
		tv_title_title.setText(titleName);
		tv_title_option.setBackgroundResource(R.drawable.btn_refresh_selecter);
		// #####################
		// sunan,20150128
		List<String> zoom_10000 = new ArrayList<String>();
		zoom_10000.add("^EURUSD");
		zoom_10000.add("^GBPUSD");
		zoom_10000.add("^AUDUSD");
		zoom_10000.add("^USDCHF");
		zoom_10000.add("^NZDUSD");
		zoom_10000.add("$DXY");
		zoom_10000.add("^USDJPY");
		List<String> zoom_1000 = new ArrayList<String>();
		zoom_1000.add("hf_OIL");
		zoom_1000.add("hf_CL");
		zoom_1000.add("hf_SI");
		zoom_1000.add("AU0");
		zoom_1000.add("NI");
		zoom_1000.add("PT");
		zoom_1000.add("PD");
		zoom_1000.add("XAG");
		zoom_1000.add("AU9999");
		zoom_1000.add("AUT+D");
		zoom_1000.add("PT9995");
		zoom_1000.add("hf_OIL");
		if (zoom_10000.contains(tradeCode)) {
			this.AxisCalc = 10000;
		} else if (zoom_1000.contains(tradeCode)) {
			this.AxisCalc = 100;
		}
		bollmaslipcandlestickchart.AxisCalc = this.AxisCalc;

		rb_bottom_nav.setOnCheckedChangeListener(this);
		presenter = new PriceDetailPreImpl(this);
		indicatorList = presenter.initIndicatorData(indicatorList);
		cycleList = presenter.initCycleData(cycleList);
		kChartList = new ArrayList<>();
		threadPriceStart = true; // 行情线程开始
		priceVisible = true;
		setTimeOrKChart();// 默认是K线
		presenter.getPrice(TAG, true, tradeCode);
		processCycle(cyclePosition);
	}

	@OnClick({ R.id.tv_title_back, R.id.tv_title_option, R.id.tv_cycle,
			R.id.tv_indicator, R.id.rb_nav_locale, R.id.rb_nav_acttrad })
	public void setOnClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_back:
			finish();
			break;
		case R.id.tv_title_option:
			refreshData();
			break;
		case R.id.tv_cycle: // 周期
			if (cycleDialog == null) {
				cycleDialog = new ListViewDialog(mContext, cycleList,
						new ListViewDialog.ListDialogItemListener() {

							@Override
							public void listItemListener(int position) {
								processCycle(position);
							}
						}, 4);
			}
			cycleDialog.setSelectPosition(cyclePosition);
			cycleDialog.show();
			break;
		case R.id.tv_indicator: // 指标
			if (indicatorDialog == null) {
				indicatorDialog = new ListViewDialog(mContext, indicatorList,
						new ListViewDialog.ListDialogItemListener() {

							@Override
							public void listItemListener(int position) {
								processIndicator(position);
							}
						}, 0);
			}
			indicatorDialog.setSelectPosition(indicatorPosition);
			indicatorDialog.show();

			break;
		case R.id.rb_nav_locale: // 本地提醒
			if (PreciousMetalAplication.getINSTANCE().user == null) {
				LoginActivity.actionLaunch(mContext);
				return;
			}
			String localSellPrice = getIntent().getStringExtra("sellPrice");
			String localIncrease = getIntent().getStringExtra("increase");
			if (currentPrice != null) {
				localSellPrice = currentPrice.sell + "";
				localIncrease = MathUtil.keep2Decimal((Double
						.parseDouble(currentPrice.last) - Double
						.parseDouble(currentPrice.lastclose)));

			}
			PriceAlertActivity.actionLaunch(mContext, tradeCode, titleName,
					localSellPrice, localIncrease, 1, 1);
			break;
		case R.id.rb_nav_acttrad: // 实盘交易
			BloomplusTradeV2.startTrade(mContext);
			break;
		}
	}

	@Override
	public String getRequestTag() {
		return TAG;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_time_pic: // 分时图
			type = TIME_CHART;
			setTimeOrKChart();
			priceTimeVisible = true; // 分时图显示
			if (!threadPriceTimeStart) {
				threadPriceTimeStart = true;
				presenter.getPriceTime(TAG, true, tradeCode);
			}

			break;
		case R.id.rb_k_line: // k线
			MessageToast.showToast("k线", 0);
			type = K_CHART;
			setTimeOrKChart();
			priceTimeVisible = false; // 分时图显示
			presenter.getPriceK(TAG, true, tradeCode,
					cycleList.get(cyclePosition).value);
			break;
		}
	}

	private void processCycle(int position) {
		cyclePosition = position;
		presenter
				.getPriceK(TAG, true, tradeCode, cycleList.get(position).value);
	}

	private void processIndicator(int position) {
		if (kChartList == null || kChartList.size() == 0) {
			return;
		}
		switch (position) {
		case INDICATOR_MACD:// MACD指标
			setKChartBottomData(kChartList, INDICATOR_MACD);
			indicatorPosition = INDICATOR_MACD;
			indicatorBottom = INDICATOR_MACD;
			break;
		case INDICATOR_BOLL:// BOLL布林线
			setKChartTopData(kChartList, INDICATOR_BOLL);
			indicatorPosition = INDICATOR_BOLL;
			indicatorTop = INDICATOR_BOLL;
			break;
		case INDICATOR_KDJ:// KDJ指标
			setKChartBottomData(kChartList, INDICATOR_KDJ);
			indicatorPosition = INDICATOR_KDJ;
			indicatorBottom = INDICATOR_KDJ;
			break;
		case INDICATOR_RSI:// RSI强弱指标
			setKChartBottomData(kChartList, INDICATOR_RSI);
			indicatorPosition = INDICATOR_RSI;
			indicatorBottom = INDICATOR_RSI;
			break;
		case INDICATOR_EMA:// EMA线
			setKChartTopData(kChartList, INDICATOR_EMA);
			indicatorPosition = INDICATOR_EMA;
			indicatorTop = INDICATOR_EMA;
			break;
		case INDICATOR_SMA:// SMA线
			setKChartTopData(kChartList, INDICATOR_SMA);
			indicatorPosition = INDICATOR_SMA;
			indicatorTop = INDICATOR_SMA;
			break;
		case INDICATOR_ADX:// ADX指标
			setKChartBottomData(kChartList, INDICATOR_ADX);
			indicatorPosition = INDICATOR_ADX;
			indicatorBottom = INDICATOR_ADX;
			break;
		case INDICATOR_ATR:// ATR指标
			setKChartBottomData(kChartList, INDICATOR_ATR);
			indicatorPosition = INDICATOR_ATR;
			indicatorBottom = INDICATOR_ATR;
			break;
		case INDICATOR_DMI:// DMI指标
			setKChartBottomData(kChartList, INDICATOR_DMI);
			indicatorPosition = INDICATOR_DMI;
			indicatorBottom = INDICATOR_DMI;
			break;
		}
		setKChartBottomTitle(bollmaslipcandlestickchart.getSelectedIndex(),
				indicatorBottom);
	}

	@Override
	public void setPrice(Price price) {
		currentPrice = price;
		// 初始化数据
		tvPriceBuy.setText(priceUtil.formatNum(Double.parseDouble(price.buy),
				price.code)); // 买价
		tvPriceSell.setText(priceUtil.formatNum(Double.parseDouble(price.sell),
				price.code)); // 卖
		tvPriceMaxvalue.setText(priceUtil.formatNum(
				Double.parseDouble(price.high), price.code)); // 最高
		tvPriceMinvalue.setText(priceUtil.formatNum(
				Double.parseDouble(price.low), price.code)); // 最低
		// 涨价
		tvPriceRise.setText(priceUtil.formatNum(Double.parseDouble(price.last)
				- Double.parseDouble(price.lastclose), price.code));
		double increasePercent = ((Double.parseDouble(price.last) - Double
				.parseDouble(price.lastclose)) / Double
				.parseDouble(price.lastclose)) * 100;
		// 涨福
		tvPriceRange.setText(String.format("%.2f", increasePercent) + "%");
		// 开盘价
		tvOpenPrice.setText(Html.fromHtml("开盘: "
				+ "<font color=#F34F4F>"
				+ priceUtil.formatNum(Double.parseDouble(price.open),
						price.code) + "</font>"));
		// 昨收
		tvYestPrice.setText("昨收: "
				+ priceUtil.formatNum(Double.parseDouble(price.lastclose),
						price.code));
		// 振幅
		tvPriceShake.setText("振幅: "
				+ priceUtil.getSwing(Double.parseDouble(price.high),
						Double.parseDouble(price.low),
						Double.parseDouble(price.open)));
		closePrice = Double.parseDouble(price.lastclose);
		// priceIsSuccess = true;
	}

	@Override
	public void showLoading() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(mContext, false);
		}
		if (!loadingDialog.isShowing()) {
			loadingDialog.show();
		}
	}

	@Override
	public void hideLoading() {
		if (loadingDialog != null) {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	@Override
	public void showToastNetErro() {
		MessageToast.showToast(R.string.net_error, 0);
		if (loadingDialog != null) {
			loadingDialog.dismiss();
		}
	}

	@Override
	public void showToast(String message) {
		MessageToast.showToast(message, 0);
	}

	@Override
	public void setPriceKList(List<Price> mList) {
		kChartList.clear();
		for (Price priceK : mList) {
			Double open = Double.parseDouble(priceK.open);
			Double high = Double.parseDouble(priceK.high);
			Double low = Double.parseDouble(priceK.low);
			Double close = Double.parseDouble(priceK.close);
			Long date = 0L;
			if (!TextUtils.isEmpty(priceK.statisticsTime)) {
				date = Long.parseLong(priceK.statisticsTime);
			}

			// ################################
			// sunan,20150128
			open = open * AxisCalc;
			high = high * AxisCalc;
			low = low * AxisCalc;
			close = close * AxisCalc;
			// #################################

			kChartList.add(0, new OHLCEntity(open, high, low, close, date));
		}
		setKChartTopData(kChartList, INDICATOR_SMA);
		setKChartBottomData(kChartList, INDICATOR_MACD);
		tvPriceTime.setText(cycleList.get(cyclePosition).key);
		indicatorPosition = INDICATOR_MACD;
		indicatorBottom = INDICATOR_MACD;
	}

	@Override
	public void setPriceTimeList(List<Price> timeList) {
		List<DateValueEntity> dateValueList = new ArrayList<DateValueEntity>();
		for (Price price : timeList) {
			long datelong = Long.parseLong(price.statisticsTime);
			// Float value = Float.parseFloat(price.getLast());
			Float value = Float.parseFloat(price.close + "");
			DateValueEntity entity = new DateValueEntity(value, datelong);
			dateValueList.add(0, entity);
		}
		for (int i = 0, k = timeList.size(); i < 240 - k; i++) {
			long datelong = 0;
			Float value = 0f;
			DateValueEntity entity = new DateValueEntity(value, datelong);
			dateValueList.add(entity);
		}
		setTimeChartData(dateValueList);
		tvPriceTime.setText(getString(R.string.price_chart_time));
	}

	/**
	 * @param dateList
	 * @Description:设置分时图数据
	 * @author: qiaoshl
	 * @date:2014-5-7
	 */

	private void setTimeChartData(List<DateValueEntity> dateList) {

		setKToptipEmpty();
		List<LineEntity<DateValueEntity>> lines = new ArrayList<LineEntity<DateValueEntity>>();

		LineEntity<DateValueEntity> dayLine = new LineEntity<DateValueEntity>();
		dayLine.setTitle("dayLine");
		dayLine.setLineColor(PriceDetailK.getColor(mContext,
				R.color.chart_line_white));
		dayLine.setLineData(dateList);
		lines.add(dayLine);

		List<DateValueEntity> closeDataList = new ArrayList<DateValueEntity>();
		for (DateValueEntity lineEntity : dateList) {
			DateValueEntity valueEntity = new DateValueEntity(
					Float.parseFloat(closePrice + ""), lineEntity.getDate());
			closeDataList.add(valueEntity);
		}
		LineEntity<DateValueEntity> closeLine = new LineEntity<DateValueEntity>();
		closeLine.setTitle("closeLine");
		closeLine.setLineColor(PriceDetailK.getColor(mContext,
				R.color.chart_line_white));
		closeLine.setLineData(closeDataList);
		lines.add(closeLine);

		PriceDetailK.initSlipLineChartTime(mContext, dateList.size(),
				slipLineChartTime);
		slipLineChartTime.setcurrentSeletedListerne(new seletedIndexListener() {

			@Override
			public void seletedIndex(int index) {
				setTimeTitle(index);
			}
		});
		slipLineChartTime.setLinesData(lines);
		int index = slipLineChartTime.getSelectedIndex();
		setTimeTitle(index);
		slipLineChartTime.notifyEvent(slipLineChartTime);
	}

	@Override
	protected void onStop() {
		super.onStop();
		priceVisible = false;
		priceTimeVisible = false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		priceVisible = true;
		priceTimeVisible = true;
		refreshData();
	}

	/**
	 * 根据当前的类型刷新数据
	 */
	private void refreshData() {
		if (type == K_CHART) {
			// K线图
			// if (!threadPriceStart) {
			// threadPriceStart = true;
			presenter.getPriceK(TAG, true, tradeCode,
					cycleList.get(cyclePosition).value);
			// }

		} else {
			// 分时图
			if (!threadPriceTimeStart) {
				threadPriceTimeStart = true;
				presenter.getPriceTime(TAG, true, tradeCode);
			}
		}
	}

	@Override
	public void priceKNetReturn() {
		threadPriceStart = false; // 执行完成
		if (priceVisible && !threadPriceStart) {
			// 行情是可见的，并且线程没有开始
			priceHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (priceVisible && !threadPriceStart) {
						threadPriceStart = true;
						Logger.i(TAG, "线程获取----getprice");
						presenter.getPriceK(TAG, true, tradeCode,
								cycleList.get(cyclePosition).value);
					}

				}
			}, Constants.PRICE_INTERVAL);
		}
	}

	@Override
	public void priceTimeNetReturn() {
		threadPriceTimeStart = false; // 线程开始为false
		if (priceTimeVisible && !threadPriceTimeStart) {
			priceHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (priceTimeVisible && !threadPriceTimeStart) {
						threadPriceTimeStart = true;
						Logger.i(TAG, "线程获取----分时图----");
						presenter.getPriceTime(TAG, false, tradeCode);
					}
				}
			}, Constants.PRICE_CHART_TIME_DURING);
		}
	}

	/**
	 * 设置是分时图还是k线图
	 */
	private void setTimeOrKChart() {
		switch (type) {
		case K_CHART: // k线图
			setKAttributeDefault();
			break;
		case TIME_CHART: // 分时图
			setTimeAttributeDefault();
			break;
		}
	}

	/**
	 * 设置分时图显示的内容
	 */
	private void setTimeAttributeDefault() {

		tvCycle.setVisibility(View.INVISIBLE);
		tvIndicator.setVisibility(View.INVISIBLE);
		rlPriceKContent.setVisibility(View.GONE);
		llTopTip1.setVisibility(View.GONE);
		llTopTip2.setVisibility(View.GONE);
		llBottomTip.setVisibility(View.GONE);
		macdchart.setVisibility(View.GONE);
		sliplinechart.setVisibility(View.GONE);

		llTimeTop.setVisibility(View.VISIBLE);
		slipLineChartTime.setVisibility(View.VISIBLE);
		setKToptipEmpty();
		setKBottomtipEmpty();
		priceTimeVisible = false; // 分时图是隐藏了
		priceVisible = false;
		// btn_time.setClickable(false);
		// btn_time.setBackgroundResource(R.drawable.chart_bottom_left_c);
		// btn_time.setTextColor(getResources().getColor(R.color.chart_text_c));
	}

	private void setKAttributeDefault() {
		setKToptipEmpty();
		setKBottomtipEmpty();

		tvCycle.setVisibility(View.VISIBLE);
		tvIndicator.setVisibility(View.VISIBLE);

		rlPriceKContent.setVisibility(View.VISIBLE);
		llTopTip1.setVisibility(View.VISIBLE);
		llTopTip2.setVisibility(View.VISIBLE);
		llBottomTip.setVisibility(View.VISIBLE);
		macdchart.setVisibility(View.VISIBLE);
		sliplinechart.setVisibility(View.GONE);

		llTimeTop.setVisibility(View.GONE);
		slipLineChartTime.setVisibility(View.GONE);
		priceTimeVisible = true; // 分时图是隐藏了
		priceVisible = true;

	}
	

	private void setKChartTopData(List<IStickEntity> ohlcEntities, int indicator) {
		PriceDetailK.initMASlipCandleStickChart(mContext, ohlcEntities.size(),
				bollmaslipcandlestickchart, macdchart);
		bollmaslipcandlestickchart
				.setStickData(new ListChartData<IStickEntity>(ohlcEntities));
		List<LineEntity<DateValueEntity>> lines = new ArrayList<LineEntity<DateValueEntity>>();

		List<LineEntity<DateValueEntity>> bollLines = new ArrayList<LineEntity<DateValueEntity>>();
		LineEntity<DateValueEntity> bollM = new LineEntity<DateValueEntity>();
		bollM.setTitle("BOLLM");
		bollM.setLineColor(PriceDetailK.getColor(mContext,
				R.color.chart_line_red));
		bollM.setLineData(Utils.getBOLL(ohlcEntities).get(
				Utils.INDICATOR_BOLL_MID));
		bollLines.add(bollM);

		LineEntity<DateValueEntity> bollU = new LineEntity<DateValueEntity>();
		bollU.setTitle("BOLLU");
		bollU.setLineColor(PriceDetailK.getColor(mContext,
				R.color.chart_line_yellow));
		bollU.setLineData(Utils.getBOLL(ohlcEntities).get(
				Utils.INDICATOR_BOLL_UP));
		bollLines.add(bollU);

		LineEntity<DateValueEntity> bollL = new LineEntity<DateValueEntity>();
		bollL.setTitle("BOLLL");
		bollL.setLineColor(PriceDetailK.getColor(mContext,
				R.color.chart_line_purple));
		bollL.setLineData(Utils.getBOLL(ohlcEntities).get(
				Utils.INDICATOR_BOLL_LOW));
		bollLines.add(bollL);

		bollmaslipcandlestickchart.setBandData(bollLines);
		bollmaslipcandlestickchart
				.setcurrentSeletedListerne(new seletedIndexListener() {

					@Override
					public void seletedIndex(int index) {
						setKChartTopTitle(index, indicatorTop);
						setKChartBottomTitle(index, indicatorBottom);
					}
				});
		switch (indicator) {
		case INDICATOR_SMA: // 简单移动平均线（SMA）
			// 计算5日均线
			SharedPreferences sp = getSharedPreferences("Price",
					Context.MODE_PRIVATE);
			int type = sp.getInt("data_select", 3);
			int sumType=sp.getInt("data_class", 0);
			LineEntity<DateValueEntity> sma5 = new LineEntity<DateValueEntity>();
			sma5.setTitle("SMA5");
			sma5.setLineColor(getResources().getColor(R.color.chart_line_white));
			sma5.setLineData(Utils.getmovingAverage(ohlcEntities, type, sumType,Integer.parseInt(sp.getString("N1", "5"))));
			lines.add(sma5);

			// 计算10日均线aaa
			LineEntity<DateValueEntity> sma10 = new LineEntity<DateValueEntity>();
			sma10.setTitle("SMA10");
			sma10.setLineColor(getResources().getColor(
					R.color.chart_line_yellow));
			sma10.setLineData(Utils.getmovingAverage(ohlcEntities, type,sumType, Integer.parseInt(sp.getString("N2", "10"))));
			lines.add(sma10);

			// 计算20日均线
			LineEntity<DateValueEntity> sma20 = new LineEntity<DateValueEntity>();
			sma20.setTitle("SMA20");
			sma20.setLineColor(getResources().getColor(R.color.chart_line_red));
			sma20.setLineData(Utils.getmovingAverage(ohlcEntities, type, sumType,Integer.parseInt(sp.getString("N3", "20"))));
			lines.add(sma20);

			// 计算60日均线
			LineEntity<DateValueEntity> sma60 = new LineEntity<DateValueEntity>();
			sma60.setTitle("SMA60");
			sma60.setLineColor(getResources().getColor(R.color.chart_line_white));
			sma60.setLineData(Utils.getmovingAverage(ohlcEntities, type, sumType,Integer.parseInt(sp.getString("N4", "60"))));
			lines.add(sma60);

			bollmaslipcandlestickchart.setLinesData(lines);
			bollmaslipcandlestickchart.notifyEvent(bollmaslipcandlestickchart);

			setKChartTopTitle(bollmaslipcandlestickchart.getSelectedIndex(),
					INDICATOR_SMA);
			break;
		case INDICATOR_BOLL:
			bollmaslipcandlestickchart.setLinesData(bollLines);
			bollmaslipcandlestickchart.notifyEvent(bollmaslipcandlestickchart);

			setKChartTopTitle(bollmaslipcandlestickchart.getSelectedIndex(),
					INDICATOR_BOLL);
			break;
		case INDICATOR_EMA:

			LineEntity<DateValueEntity> ema12 = new LineEntity<DateValueEntity>();
			ema12.setTitle("EMA12");
			ema12.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_red));
			ema12.setLineData(Utils.getEma(ohlcEntities, 12));
			lines.add(ema12);

			LineEntity<DateValueEntity> ema26 = new LineEntity<DateValueEntity>();
			ema26.setTitle("EMA26");
			ema26.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			ema26.setLineData(Utils.getEma(ohlcEntities, 26));
			lines.add(ema26);

			bollmaslipcandlestickchart.setLinesData(lines);
			bollmaslipcandlestickchart.notifyEvent(bollmaslipcandlestickchart);

			setKChartTopTitle(bollmaslipcandlestickchart.getSelectedIndex(),
					INDICATOR_EMA);
			break;
		}
		bollmaslipcandlestickchart.addNotify(new ITouchEventResponse() {

			@Override
			public void notifyEvent(GridChart chart) {
				MASlipCandleStickChart maSlipCandleStickChart = (MASlipCandleStickChart) chart;

				macdchart.setDisplayFrom(maSlipCandleStickChart
						.getDisplayFrom());
				macdchart.setDisplayNumber(maSlipCandleStickChart
						.getDisplayNumber());
				macdchart.notifyEvent(maSlipCandleStickChart);

				sliplinechart.setDisplayFrom(maSlipCandleStickChart
						.getDisplayFrom());
				sliplinechart.setDisplayNumber(maSlipCandleStickChart
						.getDisplayNumber());
				sliplinechart.notifyEvent(chart);
			}
		});
	}

	private void setKChartTopTitle(int currentIndex, int indicator) {
		setKToptipEmpty();
		IChartData<IStickEntity> iChartData = bollmaslipcandlestickchart
				.getStickData();
		if (iChartData == null || iChartData.size() == 0) {
			return;
		}
		OHLCEntity ohlcEntity = (OHLCEntity) iChartData.get(currentIndex);

		String datestr = DateUtil.converTime(ohlcEntity.getDate(),
				"yyyy-MM-dd HH:mm", 0);
		tvKDate.setText(datestr);
		switch (indicator) {
		case INDICATOR_SMA:
			LineEntity<DateValueEntity> sma5 = bollmaslipcandlestickchart
					.getLinesData().get(0);
			LineEntity<DateValueEntity> sma10 = bollmaslipcandlestickchart
					.getLinesData().get(1);
			LineEntity<DateValueEntity> sma20 = bollmaslipcandlestickchart
					.getLinesData().get(2);
			LineEntity<DateValueEntity> sma60 = bollmaslipcandlestickchart
					.getLinesData().get(3);
			DateValueEntity maFir = sma5.getLineData().get(currentIndex);
			DateValueEntity maMid = sma10.getLineData().get(currentIndex);
			DateValueEntity maLast = sma20.getLineData().get(currentIndex);
			DateValueEntity maLastL = sma60.getLineData().get(currentIndex);
			// ###########################
			// sunan,20150128
			tvTipOpen.setText(getString(R.string.price_chart_open)
					+ MathUtil.keep2Decimal(ohlcEntity.getOpen() / AxisCalc));
			tvTipYest.setText(getString(R.string.price_chart_close)
					+ MathUtil.keep2Decimal(ohlcEntity.getClose() / AxisCalc));
			tvTipHigh.setText(getString(R.string.price_chart_high)
					+ MathUtil.keep2Decimal(ohlcEntity.getHigh() / AxisCalc));
			tvTipLow.setText(getString(R.string.price_chart_low)
					+ MathUtil.keep2Decimal(ohlcEntity.getLow() / AxisCalc));
			tvTipInd5.setText(getString(R.string.price_chart_maFirst)
					+ MathUtil.keep2Decimal(maFir.getValue() / AxisCalc));
			tvTipInd10.setText(getString(R.string.price_chart_maMid)
					+ MathUtil.keep2Decimal(maMid.getValue() / AxisCalc));
			tvTipInd20.setText(getString(R.string.price_chart_maLast)
					+ MathUtil.keep2Decimal(maLast.getValue() / AxisCalc));
			tvTipInd60.setText(getString(R.string.price_chart_maLastL)
					+ MathUtil.keep2Decimal(maLastL.getValue() / AxisCalc));
			// ###########################
			if (currentIndex > 0) {
				OHLCEntity lastOhlcEntity = (OHLCEntity) iChartData
						.get(currentIndex - 1);
				setKChartTopColor(ohlcEntity, lastOhlcEntity.getClose());
				// tvTipInd5.setTextColor(sma5.getLineColor());
				tvTipInd5.setTextColor(getResources().getColor(R.color.gray));
				tvTipInd10.setTextColor(sma10.getLineColor());
				tvTipInd20.setTextColor(sma20.getLineColor());
			} else {
				setKChartTopColor(ohlcEntity, 0);
				// tvTipInd5.setTextColor(sma5.getLineColor());
				tvTipInd5.setTextColor(getResources().getColor(R.color.gray));
				tvTipInd10.setTextColor(sma10.getLineColor());
				tvTipInd20.setTextColor(sma20.getLineColor());
			}
			break;
		case INDICATOR_BOLL:
			// ###########################
			// sunan,20150128
			tvTipOpen.setText(getString(R.string.price_chart_open)
					+ MathUtil.keep2Decimal(ohlcEntity.getOpen() / AxisCalc));
			tvTipYest.setText(getString(R.string.price_chart_close)
					+ MathUtil.keep2Decimal(ohlcEntity.getClose() / AxisCalc));
			tvTipHigh.setText(getString(R.string.price_chart_high)
					+ MathUtil.keep2Decimal(ohlcEntity.getHigh() / AxisCalc));
			tvTipLow.setText(getString(R.string.price_chart_low)
					+ MathUtil.keep2Decimal(ohlcEntity.getLow() / AxisCalc));
			// #############################
			DateValueEntity ma = bollmaslipcandlestickchart.getLinesData()
					.get(0).getLineData().get(currentIndex);
			DateValueEntity upboll = bollmaslipcandlestickchart.getLinesData()
					.get(1).getLineData().get(currentIndex);
			DateValueEntity dn = bollmaslipcandlestickchart.getLinesData()
					.get(2).getLineData().get(currentIndex);

			if (currentIndex > 0) {
				OHLCEntity lastOhlcEntity = (OHLCEntity) iChartData
						.get(currentIndex - 1);
				setKChartTopColor(ohlcEntity, lastOhlcEntity.getClose());
			} else {
				setKChartTopColor(ohlcEntity, 0);
			}
			// sunan,20150128
			tvTipInd5.setText(" MA:"
					+ MathUtil.keep2Decimal(ma.getValue() / AxisCalc));
			tvTipInd5.setVisibility(View.VISIBLE);
			// tvTipInd5.setTextColor(bollmaslipcandlestickchart.getLinesData().get(0).getLineColor());
			// sunan,20150128
			tvTipInd10.setText(" UP:"
					+ MathUtil.keep2Decimal(upboll.getValue() / AxisCalc));
			tvTipInd10.setVisibility(View.VISIBLE);
			// tvTipInd10.setTextColor(bollmaslipcandlestickchart.getLinesData().get(1).getLineColor());
			// sunan,20150128
			tvTipInd20.setText(" DN:"
					+ MathUtil.keep2Decimal(dn.getValue() / AxisCalc));
			tvTipInd20.setVisibility(View.VISIBLE);
			// tvTipInd20.setTextColor(bollmaslipcandlestickchart.getLinesData().get(2).getLineColor());
			break;
		case INDICATOR_EMA:
			// ##########################
			// sunan,20150128
			tvTipOpen.setText(getString(R.string.price_chart_open)
					+ MathUtil.keep2Decimal(ohlcEntity.getOpen() / AxisCalc));
			tvTipYest.setText(getString(R.string.price_chart_close)
					+ MathUtil.keep2Decimal(ohlcEntity.getClose() / AxisCalc));
			tvTipHigh.setText(getString(R.string.price_chart_high)
					+ MathUtil.keep2Decimal(ohlcEntity.getHigh() / AxisCalc));
			tvTipLow.setText(getString(R.string.price_chart_low)
					+ MathUtil.keep2Decimal(ohlcEntity.getLow() / AxisCalc));
			// ##########################
			DateValueEntity up = bollmaslipcandlestickchart.getLinesData()
					.get(0).getLineData().get(currentIndex);
			DateValueEntity low = bollmaslipcandlestickchart.getLinesData()
					.get(1).getLineData().get(currentIndex);

			if (currentIndex > 0) {
				OHLCEntity lastOhlcEntity = (OHLCEntity) iChartData
						.get(currentIndex - 1);
				setKChartTopColor(ohlcEntity, lastOhlcEntity.getClose());
			} else {
				setKChartTopColor(ohlcEntity, 0);
			}
			// sunan,20150128
			tvTipInd5.setText(" EMA(12):"
					+ MathUtil.keep2Decimal(up.getValue() / AxisCalc));
			tvTipInd5.setVisibility(View.VISIBLE);
			// tvTipInd5.setTextColor(bollmaslipcandlestickchart.getLinesData().get(0).getLineColor());
			// sunan,20150128
			tvTipInd10.setText(" EMA(26):"
					+ MathUtil.keep2Decimal(low.getValue() / AxisCalc));
			tvTipInd10.setVisibility(View.VISIBLE);
			// tvTipInd10.setTextColor(bollmaslipcandlestickchart.getLinesData().get(1).getLineColor());

			break;
		}
	}

	private void setKChartTopColor(OHLCEntity ohlcEntity, double lastClose) {
		double open = ohlcEntity.getOpen();
		double close = ohlcEntity.getClose();
		double high = ohlcEntity.getHigh();
		double low = ohlcEntity.getLow();
		// 设置标签文本颜色
		if (open == 0) {
			tvTipOpen.setTextColor(PriceDetailK.getColor(mContext,
					R.color.black));
		} else {
			// 设置标签文本颜色
			tvTipOpen
					.setTextColor(open != lastClose ? open > lastClose ? PriceDetailK
							.getColor(mContext, R.color.chart_line_red)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_green)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_gray));
		}

		if (high == 0) {
			tvTipYest.setTextColor(PriceDetailK.getColor(mContext,
					R.color.black));
		} else {
			// 设置标签文本颜色
			tvTipYest
					.setTextColor(high != lastClose ? high > lastClose ? PriceDetailK
							.getColor(mContext, R.color.chart_line_red)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_green)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_gray));
		}

		if (low == 0) {
			tvTipHigh.setTextColor(PriceDetailK.getColor(mContext,
					R.color.black));
		} else {
			tvTipHigh
					.setTextColor(low != lastClose ? low > lastClose ? PriceDetailK
							.getColor(mContext, R.color.chart_line_red)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_green)
							: PriceDetailK.getColor(mContext,
									R.color.chart_line_gray));
		}

		if (close == 0) {
			tvTipLow.setTextColor(PriceDetailK
					.getColor(mContext, R.color.black));
		} else {
			tvTipLow.setTextColor(close != lastClose ? close > lastClose ? PriceDetailK
					.getColor(mContext, R.color.chart_line_red) : PriceDetailK
					.getColor(mContext, R.color.chart_line_green)
					: PriceDetailK.getColor(mContext, R.color.chart_line_gray));
		}
	}

	/**
	 * 设置底下的图
	 * 
	 * @param ohlcEntities
	 * @param indicator
	 */
	private void setKChartBottomData(List<IStickEntity> ohlcEntities,
			int indicator) {

		int currentIndex;
		List<LineEntity<DateValueEntity>> lines = new ArrayList<LineEntity<DateValueEntity>>();
		switch (indicator) {
		case INDICATOR_MACD: // 底下显示macd图
			macdchart.setVisibility(View.VISIBLE);
			sliplinechart.setVisibility(View.GONE);

			PriceDetailK.iniMACDChart(mContext, ohlcEntities.size(), macdchart);
			macdchart.setStickData(new ListChartData<IStickEntity>(Utils
					.getMACD(ohlcEntities)));
			macdchart.notifyEvent(macdchart);

			currentIndex = macdchart.getSelectedIndex();
			setKChartBottomTitle(currentIndex, INDICATOR_MACD);
			break;
		case INDICATOR_KDJ: // kdj随机指标
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			Map<String, List<DateValueEntity>> map = Utils.getKDJ(ohlcEntities);

			LineEntity<DateValueEntity> lineK = new LineEntity<DateValueEntity>();
			List<DateValueEntity> kdjKData = map.get(Utils.INDICATOR_KDJ_K);
			lineK.setTitle("kdjK");
			lineK.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_red));
			lineK.setLineData(kdjKData);
			lines.add(lineK);

			LineEntity<DateValueEntity> lineD = new LineEntity<DateValueEntity>();
			List<DateValueEntity> kdjDData = map.get(Utils.INDICATOR_KDJ_D);
			lineD.setTitle("kdjD");
			lineD.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			lineD.setLineData(kdjDData);
			lines.add(lineD);

			LineEntity<DateValueEntity> lineJ = new LineEntity<DateValueEntity>();
			List<DateValueEntity> kdjJDData = map.get(Utils.INDICATOR_KDJ_J);
			lineJ.setTitle("kdjJ");
			lineJ.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_white));
			lineJ.setLineData(kdjJDData);
			lines.add(lineJ);

			PriceDetailK.initSlipLineChartK(mContext, kdjDData.size(),
					sliplinechart);
			sliplinechart.setLinesData(lines);
			sliplinechart.notifyEvent(sliplinechart);

			currentIndex = sliplinechart.getSelectedIndex();
			setKChartBottomTitle(macdchart.getSelectedIndex(), INDICATOR_KDJ);
			break;
		case INDICATOR_RSI:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> lineR = new LineEntity<DateValueEntity>();
			List<DateValueEntity> lineData6 = Utils.getRsi(ohlcEntities, 6);
			lineR.setTitle("rsi6");
			lineR.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_red));
			lineR.setLineData(lineData6);
			lines.add(lineR);

			LineEntity<DateValueEntity> lineS = new LineEntity<DateValueEntity>();
			lineS.setTitle("rsi12");
			lineS.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			lineS.setLineData(Utils.getRsi(ohlcEntities, 12));
			lines.add(lineS);

			PriceDetailK.initSlipLineChartK(mContext, lineData6.size(),
					sliplinechart);
			sliplinechart.setLinesData(lines);

			sliplinechart.notifyEvent(sliplinechart);
			currentIndex = sliplinechart.getSelectedIndex();
			setKChartBottomTitle(macdchart.getSelectedIndex(), INDICATOR_RSI);
			break;
		case INDICATOR_ADX:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> lineADX9 = new LineEntity<DateValueEntity>();

			List<DateValueEntity> adx9List = Utils.getAdx(ohlcEntities, 9);
			lineADX9.setTitle("lineADX9");
			lineADX9.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			lineADX9.setLineData(adx9List);
			lines.add(lineADX9);

			PriceDetailK.initSlipLineChartK(mContext, adx9List.size(),
					sliplinechart);
			sliplinechart.setLinesData(lines);
			sliplinechart.notifyEvent(sliplinechart);

			currentIndex = sliplinechart.getSelectedIndex();
			setKChartBottomTitle(macdchart.getSelectedIndex(), INDICATOR_ADX);
			break;
		case INDICATOR_ATR:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> lineAtr15 = new LineEntity<DateValueEntity>();

			List<DateValueEntity> atr15List = Utils.getAtr(ohlcEntities, 15);
			lineAtr15.setTitle("lineAtr15");
			lineAtr15.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			lineAtr15.setLineData(atr15List);
			lines.add(lineAtr15);

			PriceDetailK.initSlipLineChartK(mContext, atr15List.size(),
					sliplinechart);
			sliplinechart.setLinesData(lines);
			sliplinechart.notifyEvent(sliplinechart);

			currentIndex = sliplinechart.getSelectedIndex();
			setKChartBottomTitle(macdchart.getSelectedIndex(), INDICATOR_ATR);
			break;
		case INDICATOR_DMI:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);
			// macdchart.setVisibility(View.GONE);
			// sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> lineDmi20 = new LineEntity<DateValueEntity>();

			List<DateValueEntity> atr20List = Utils.getDmi(ohlcEntities, 20);
			lineDmi20.setTitle("lineDmi20");
			lineDmi20.setLineColor(PriceDetailK.getColor(mContext,
					R.color.chart_line_yellow));
			lineDmi20.setLineData(atr20List);
			lines.add(lineDmi20);

			PriceDetailK.initSlipLineChartK(mContext, atr20List.size(),
					sliplinechart);
			sliplinechart.setLinesData(lines);
			sliplinechart.notifyEvent(sliplinechart);

			currentIndex = sliplinechart.getSelectedIndex();
			setKChartBottomTitle(macdchart.getSelectedIndex(), INDICATOR_DMI);
			break;
		}
	}

	private void setKChartBottomTitle(int currentIndex, int indicator) {
		setKBottomtipEmpty();
		IChartData<IStickEntity> iChartData = bollmaslipcandlestickchart
				.getStickData();
		if (iChartData == null || iChartData.size() == 0) {
			return;
		}
		OHLCEntity ohlcEntity = (OHLCEntity) iChartData.get(currentIndex);

		String datestr = DateUtil.converTime(ohlcEntity.getDate(),
				"yyyy-MM-dd HH:mm", 0);
		tvKDate.setText(datestr);

		switch (indicator) {
		case INDICATOR_MACD:
			macdchart.setVisibility(View.VISIBLE);
			sliplinechart.setVisibility(View.GONE);

			MACDEntity macdEntity = (MACDEntity) macdchart.getStickData().get(
					currentIndex);
			double dif = macdEntity.getDiff();
			double dea = macdEntity.getDea();
			double macd = macdEntity.getMacd();
			tvTipBottom1.setText(" MACD(12,26,9):"
					+ MathUtil.keep2Decimal(macd));
			tvTipBottom1.setVisibility(View.VISIBLE);
			tvTipBottom1.setTextColor(macdchart.getMacdLineColor());
			tvTipBottom2.setText(" DIFF:" + MathUtil.keep2Decimal(dif));
			tvTipBottom2.setVisibility(View.VISIBLE);
			tvTipBottom2.setTextColor(macdchart.getDiffLineColor());
			tvTipBottom3.setText(" DEA:" + MathUtil.keep2Decimal(dea));
			tvTipBottom3.setVisibility(View.VISIBLE);
			tvTipBottom3.setTextColor(macdchart.getDeaLineColor());
			break;
		case INDICATOR_KDJ:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			DateValueEntity kdjK = sliplinechart.getLinesData().get(0)
					.getLineData().get(currentIndex);
			DateValueEntity kdjD = sliplinechart.getLinesData().get(1)
					.getLineData().get(currentIndex);
			DateValueEntity kdjJ = sliplinechart.getLinesData().get(2)
					.getLineData().get(currentIndex);
			tvTipBottom1
					.setText(" K:" + MathUtil.keep2Decimal(kdjK.getValue()));
			tvTipBottom1.setTextColor(sliplinechart.getLinesData().get(0)
					.getLineColor());
			tvTipBottom2
					.setText(" D:" + MathUtil.keep2Decimal(kdjD.getValue()));
			tvTipBottom2.setTextColor(sliplinechart.getLinesData().get(1)
					.getLineColor());
			tvTipBottom3
					.setText(" J:" + MathUtil.keep2Decimal(kdjJ.getValue()));
			tvTipBottom3.setTextColor(sliplinechart.getLinesData().get(2)
					.getLineColor());
			break;
		case INDICATOR_RSI:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			DateValueEntity rsi6 = sliplinechart.getLinesData().get(0)
					.getLineData().get(currentIndex);
			DateValueEntity rsi12 = sliplinechart.getLinesData().get(1)
					.getLineData().get(currentIndex);
			tvTipBottom1.setText(" RSI6:"
					+ MathUtil.keep2Decimal(rsi6.getValue()));
			tvTipBottom1.setTextColor(sliplinechart.getLinesData().get(0)
					.getLineColor());
			tvTipBottom2.setText(" RSI12:"
					+ MathUtil.keep2Decimal(rsi12.getValue()));
			tvTipBottom2.setTextColor(sliplinechart.getLinesData().get(1)
					.getLineColor());
			break;
		case INDICATOR_ADX:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> adx9Datas = sliplinechart
					.getLinesData().get(0);
			DateValueEntity adx9 = adx9Datas.getLineData().get(currentIndex);
			tvTipBottom1.setText(" ADX9:"
					+ MathUtil.keep2Decimal(adx9.getValue()));
			tvTipBottom1.setTextColor(adx9Datas.getLineColor());
			break;
		case INDICATOR_ATR:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> atr15Line = sliplinechart
					.getLinesData().get(0);
			DateValueEntity atr15 = atr15Line.getLineData().get(currentIndex);
			tvTipBottom1.setText(" ATR15:"
					+ MathUtil.keep2Decimal(atr15.getValue()));
			tvTipBottom1.setTextColor(atr15Line.getLineColor());
			break;
		case INDICATOR_DMI:
			macdchart.setVisibility(View.GONE);
			sliplinechart.setVisibility(View.VISIBLE);

			LineEntity<DateValueEntity> dmi20Line = sliplinechart
					.getLinesData().get(0);
			DateValueEntity dx20 = dmi20Line.getLineData().get(currentIndex);
			tvTipBottom1.setText(" DX20:"
					+ MathUtil.keep2Decimal(dx20.getValue()));
			tvTipBottom1.setTextColor(dmi20Line.getLineColor());
			break;
		}
	}

	private void setKToptipEmpty() {
		tvTipOpen.setText("");
		tvTipYest.setText("");
		tvTipHigh.setText("");
		tvTipLow.setText("");
		tvTipInd5.setText("");
		tvTipInd10.setText("");
		tvTipInd20.setText("");
	}

	private void setKBottomtipEmpty() {
		tvTipBottom1.setText("");
		tvTipBottom2.setText("");
		tvTipBottom3.setText("");
	}

	private void setTimeTitle(int index) {
		LineEntity<DateValueEntity> line = slipLineChartTime.getLinesData()
				.get(0);
		StringBuilder builder = new StringBuilder();
		builder.append("run: " + line.getLineData().get(index).getValue());
		DateValueEntity valueEntity = line.getLineData().get(index);
		long date = valueEntity.getDate();
		if (date != 0) {
			String datestr = DateUtil.converTime(valueEntity.getDate(),
					"yyyy-MM-dd HH:mm", 0);
			tvTimeDate.setText(datestr);
		} else {
			tvTimeDate.setText("");
		}
		tvTimeTopRun.setText(builder.toString());

		/*
		 * tvTimeDate.setTextColor(PriceDetailK.getColor(mContext,
		 * R.color.chart_line_white));
		 * tvTimeTopRun.setTextColor(PriceDetailK.getColor(mContext,
		 * R.color.chart_line_white));
		 */
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (priceHandler != null) {
			priceHandler.removeCallbacksAndMessages(null);
		}
		if (priceTimeHandler != null) {
			priceTimeHandler.removeCallbacksAndMessages(null);
		}
	}
}
