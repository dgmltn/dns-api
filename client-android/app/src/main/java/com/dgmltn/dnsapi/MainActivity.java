package com.dgmltn.dnsapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.measite.minidns.Client;
import de.measite.minidns.DNSMessage;
import de.measite.minidns.Question;
import de.measite.minidns.Record;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

	@Bind(R.id.input)
	EditText vInput;

	@Bind(R.id.output)
	TextView vOutput;

	@Bind(R.id.host)
	TextView vHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		vHost.setText(BuildConfig.HOST_SUFFIX);
	}

	@OnClick(R.id.go)
	protected void go() {
		String input = vInput.getText().toString() + BuildConfig.HOST_SUFFIX;
		doDnsLookup(input, Record.TYPE.TXT);
	}

	protected void doDnsLookup(String hostname, Record.TYPE type) {
		Question q = new Question(hostname, type, Record.CLASS.IN);

		Observable.just(q)
			.map(new Func1<Question, DNSMessage>() {
				@Override
				public DNSMessage call(Question question) {
					return new Client().query(question);
				}
			})
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Action1<DNSMessage>() {
				@Override
				public void call(DNSMessage dnsMessage) {
					showResult(dnsMessage);
				}
			});
	}

	protected void showResult(DNSMessage dnsMessage) {
		if (dnsMessage == null) {
			vOutput.setText("no result");
		}
		else {
			String output = new String(dnsMessage.getAnswers()[0].getPayload().toByteArray());
			vOutput.setText(output);
		}
	}
}
